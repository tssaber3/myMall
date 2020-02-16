package com.tssaber.mmall.service.Impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.controller.vo.OrderVo;
import com.tssaber.mmall.dao.OrderMapper;
import com.tssaber.mmall.entity.pojo.Goods;
import com.tssaber.mmall.entity.pojo.Order;
import com.tssaber.mmall.entity.pojo.OrderItem;
import com.tssaber.mmall.entity.pojo.bo.CartOrderBo;
import com.tssaber.mmall.entity.pojo.bo.OrderBo;
import com.tssaber.mmall.entity.pojo.dto.OrderDto;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.rabbitmq.TopicSender;
import com.tssaber.mmall.redis.OrderKey;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.service.OrderService;
import com.tssaber.mmall.util.NumberUtils;
import com.tssaber.mmall.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:tssaber
 * @Date: 2020/2/2 19:40
 * @Version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private GoodsServiceImpl goodsService;

    @Resource
    private UserServiceImpl userService;

    @Resource
    private TopicSender topicSender;

    @Resource
    private CartServiceImpl cartService;

    @Resource
    private SeckillServiceImpl seckillService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private OrderMapper orderMapper;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * 处理下单 扣数据库的库存,生成订单并且落库
     * 物流编号 还没有落实
     * @param orderDto
     * @return
     */
    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public boolean processOrder(OrderDto orderDto) {
        Goods goods = goodsService.selGoodsById(orderDto.getGoodId());
        orderDto.setOrderNo(Long.valueOf(NumberUtils.getGuid()));
        OrderBo orderBo = new OrderBo(orderDto,goods);
        insOrder(orderBo.getOrder());
        redisUtils.del(OrderKey.userOrders,String.valueOf(orderBo.getOrder().getUserId()));
        insOrderItem(orderBo.getOrderItem());
        redisUtils.del(OrderKey.orderDetail,"orderItem");
        goodsService.goodsReduction(orderDto.getGoodId(),1);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insOrder(Order order) {
        orderMapper.insOrder(order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insOrderItem(OrderItem orderItem) {
        orderMapper.insOrderItem(orderItem);
//        redisUtils.hset(OrderKey.orderDetail,"orderItem",String.valueOf(orderItem.getOrderNo()),orderItem);
    }

    /**
     * 重写
     * 查询用户对应的全部order
     * 1.先查询redis
     * 2.没有再查数据库
     * 3.最后再将其放到redis中
     * @param userId:用户id
     * @return :对应的order集合
     */
    @Override
    public List<OrderVo> selAllOrderByUserId(Integer userId) {
        List<Order> orders = null;
        List<OrderVo> orderVos = new ArrayList<>();
        Map<String,OrderVo> orderMap = redisUtils.hgetAll(OrderKey.userOrders,String.valueOf(userId),OrderVo.class);
        if (orderMap != null && !orderMap.isEmpty()){
            log.info("从缓存中获取到orders");
            return new ArrayList<>(orderMap.values());
        }
        log.info("缓存中不存在orders 到数据库中查询");
        orders = orderMapper.selOrdersByUserId(userId);
        orderMap = new HashMap<>(16);
        if (orders.size() != 0){
            for (Order order:orders){
                orderMap.put(String.valueOf(order.getOrderNo()),new OrderVo(order));
                orderVos.add(new OrderVo(order));
            }
            redisUtils.hsetAll(OrderKey.userOrders,String.valueOf(userId),orderMap);
            return orderVos;
        }
        return null;
    }

    @Override
    public OrderVo selOrderDetail(Integer orderId,HttpServletRequest request) {
        UserDto userDto = userService.selUser(request);
        OrderVo item = redisUtils.hget(OrderKey.orderDetail,"orderItem",OrderVo.class,String.valueOf(orderId)).get(0);
        if (item != null){
            if (!userDto.getId().equals(item.getUserId())){
                throw new GlobalException(CExceptionEnums.BIND_ERROR);
            }
            log.info("从缓存中获取订单详情");
            return item;
        }
        log.info("缓存中不存在 到数据库中查询");
        Order order = orderMapper.selOrderByOrderId(orderId);
        if (order != null){
            if (!userDto.getId().equals(order.getUserId())){
                throw new GlobalException(CExceptionEnums.BIND_ERROR);
            }
            item = new OrderVo(order);
            item.setUserId(userDto.getId());
            redisUtils.hset(OrderKey.orderDetail,"orderItem",String.valueOf(orderId),item);
            return item;
        }
        return null;
    }

    /**
     * 先解析orderInfo数据
     * @param md5:加密的url
     * @param orderInfo:订单信息
     * @param request:用于获取用户信息
     * @return
     */
    @Override
    public boolean confirmOrder(String md5, String orderInfo, HttpServletRequest request) {
        OrderDto orderDto;
        List<Integer> goodsIdList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try {
            JsonObject object = new JsonParser().parse(orderInfo).getAsJsonObject();
            JsonArray array = object.getAsJsonArray("goodsId");
            for (int i = 0;i < array.size();i++){
                JsonObject jsonObject = (JsonObject) array.get(i);
                goodsIdList.add(jsonObject.get("goodsId").getAsInt());
                sb.append(jsonObject.get("goodsId").getAsInt());
            }
            orderDto = new OrderDto(new BigDecimal(object.get("totalPrice").getAsString()),
                    new BigDecimal(object.get("postage").getAsString()),
                    goodsIdList);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        //检验MD5的正确性
        if (md5 == null || !md5.equals(UrlUtils.getKuaishouSign(sb.toString()))){
            log.info("md5解密错误");
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }else {
            log.info("md5解析成功");
        }
        UserDto userDto = userService.selUser(request);
        orderDto.setUserId(userDto.getId());
        orderDto.setAddress(userDto.getAddress());
        System.out.println(orderDto.getAddress());
        for (Integer integer:goodsIdList){
            long leftStock = seckillService.stock(integer);
            if (leftStock == 0){
                return false;
            }
        }
        topicSender.sendCartOrder(orderDto);
        return true;
    }

    /**
     * 处理购物车订单落库
     * 订单落库之后删除购物车
     * @param orderDto
     * @return
     */
    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public boolean processOrderCart(OrderDto orderDto) {
        List<Goods> goodsList = goodsService.selAllGoodsById(orderDto.getGoodsIdList());
        orderDto.setOrderNo(Long.valueOf(NumberUtils.getGuid()));
        CartOrderBo cartOrderBo = new CartOrderBo(orderDto,goodsList);
        List<OrderItem> orderItems = cartOrderBo.getOrderItemList();
        insOrder(cartOrderBo.getOrder());
        for (OrderItem orderItem:orderItems){
            insOrderItem(orderItem);
        }
        for (Goods goods:goodsList){
            goodsService.goodsReduction(goods.getId(),1);
        }
        //删除购物车中的内容
        cartService.delCartAll(orderDto.getUserId());
        return true;
    }


}

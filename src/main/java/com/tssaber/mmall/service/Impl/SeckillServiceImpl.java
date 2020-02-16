package com.tssaber.mmall.service.Impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.entity.pojo.dto.OrderDto;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.rabbitmq.TopicSender;
import com.tssaber.mmall.redis.GoodsKey;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.redis.SeckillKey;
import com.tssaber.mmall.service.SeckillService;
import com.tssaber.mmall.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @Author:tssaber
 * @Date: 2020/2/1 21:01
 * @Version 1.0
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private GoodsServiceImpl goodsService;

    @Resource
    private UserServiceImpl userService;

    @Resource
    private TopicSender topicSender;

    private static final Logger log = LoggerFactory.getLogger(SeckillServiceImpl.class);

    /**
     * 加入混淆字符串的salt(盐),避免用户猜出MD5的值
     */
    private static final String SALT = "12sadasadsafafsafs。/。，";

    /**
     * 当库存未初始化时 返回的数
     */
    private static final long UNINITIALIZED_STOCK = -1;

    /**
     * 查询redis看看对应的商品是否还有货
     * @param goodId :商品的id
     * @return String 加密之后的部分url
     */
    @Override
    public String exportSeckillUrl(String goodId) {
        log.info("判断是否还有货");
        boolean exists = redisUtils.exists(SeckillKey.isGoodsOver,goodId);
        if (!exists){
            throw new GlobalException(CExceptionEnums.SECKILL_OVER);
        }
        return UrlUtils.getKuaishouSign(goodId);
    }

    /**
     * 1.解析md5是否正确
     * 2.解析orderInfo是否符合要求
     * 3.进行扣库存redis中
     * 4.生成订单 并落库
     * 5.进行数据库中的扣库存
     * 4.5 步骤通过MQ来实现
     * @param md5：url的加密部分
     * @param goodId 商品的id
     * @param request 用来获取用户信息
     * @return
     */
    @Override
    public Boolean executionSeckillId(String md5, Integer goodId,String orderInfo,HttpServletRequest request) {
        if (md5 == null || !md5.equals(UrlUtils.getKuaishouSign(String.valueOf(goodId)))){
            log.info("md5解密错误");
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }else {
            log.info("解析成功");
        }
        OrderDto orderDto = null;
        try {
            JsonObject object = new JsonParser().parse(orderInfo).getAsJsonObject();
            orderDto = new OrderDto(object.get("payment").getAsBigDecimal(),
                    object.get("paymentType").getAsInt(),
                    new BigDecimal(object.get("postage").getAsString()),
                    goodId);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        UserDto userDto = userService.selUser(request);
        orderDto.setUserId(userDto.getId());
        orderDto.setAddress(userDto.getAddress());
        long leftStock = stock(goodId);
        if (leftStock != 0){
            topicSender.sendOrder(orderDto);
            return true;
        }
        return false;
    }

    /**
     * 在redis中进行扣库存
     * stockTest 返回值
     * 0表示库存不足
     * -1表示该商品未初始化
     * >0表示扣库存之前的剩余库存
     * @param goodsId
     * @return
     */
    @Override
    public long stock(Integer goodsId) {
        long stock = redisUtils.stockTest(GoodsKey.getGoodsStock,String.valueOf(goodsId));
        //如果返回的是-1 表示未进行初始化 需要到数据库中进行读取
        // 这时需要加锁防止其他线程对数据库的数据进行修改
        if (stock == UNINITIALIZED_STOCK){
            try {
                if (redisUtils.tryLock(GoodsKey.goodsLock,String.valueOf(goodsId),String.valueOf(Thread.currentThread().getId()))){
                    //双重校验锁 再一次看看redis返回的是不是-1
                    stock = redisUtils.stockTest(GoodsKey.getGoodsStock,String.valueOf(goodsId));
                    if (stock == UNINITIALIZED_STOCK){
                        final int initStock = goodsService.getStock(goodsId);
                        redisUtils.set(GoodsKey.getGoodsStock,String.valueOf(goodsId),initStock);
                        //之后再进行一次扣库存的操作
                        stock = redisUtils.stockTest(GoodsKey.getGoodsStock,String.valueOf(goodsId));
                    }
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
            finally {
                redisUtils.unLock(GoodsKey.goodsLock,String.valueOf(goodsId),String.valueOf(Thread.currentThread().getId()));
            }
        }
        return stock;
    }

}

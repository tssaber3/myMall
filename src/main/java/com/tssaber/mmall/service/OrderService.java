package com.tssaber.mmall.service;

import com.tssaber.mmall.controller.vo.OrderVo;
import com.tssaber.mmall.entity.pojo.Order;
import com.tssaber.mmall.entity.pojo.OrderItem;
import com.tssaber.mmall.entity.pojo.dto.OrderDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/2 19:39
 * @Version 1.0
 */
@Service
public interface OrderService {

    /**
     * 处理下单 扣数据库的库存,生成订单并且落库
     * @param orderDto
     * @return
     */
    boolean processOrder(OrderDto orderDto);

    /**
     * 订单落库
     * @param order
     */
    void insOrder(Order order);

    /**
     * 订单明细的落库
     * @param orderItem
     */
    void insOrderItem(OrderItem orderItem);

    /**
     * 获得用户的全部订单
     * @param userId:用户id
     * @return
     */
    List<OrderVo> selAllOrderByUserId(Integer userId);

    /**
     * 获取订单详情
     * @param orderId
     * @param request
     * @return
     */
    OrderVo selOrderDetail(Integer orderId,HttpServletRequest request);

    /**
     * 解析订单消息 并且发送到mq中
     * @param md5:加密的url
     * @param orderInfo:订单信息
     * @param request:用于获取用户信息
     * @return
     */
    boolean confirmOrder(String md5, String orderInfo, HttpServletRequest request);

    /**
     * mq调用
     * 处理购物车的订单落库
     * @param orderDto
     * @return
     */
    boolean processOrderCart(OrderDto orderDto);
}

package com.tssaber.mmall.dao;

import com.tssaber.mmall.entity.pojo.Order;
import com.tssaber.mmall.entity.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/2 19:49
 * @Version 1.0
 */
@Repository
public interface OrderMapper {

    /**
     * 进行订单的落库
     * @param order
     */
    void insOrder(Order order);

    /**
     * 订单详情的落库
     * @param orderItem
     */
    void insOrderItem(OrderItem orderItem);

    /**
     * 通过用户id 获取对应的订单list
     * @param userId
     * @return
     */
    List<Order> selOrdersByUserId(@Param("userId") Integer userId);

//    /**
//     * 通过订单编号获取订单详情
//     * @param orderNo
//     * @return
//     */
//    OrderItem selOrderDetailByOrderNo(@Param("orderNo") Long orderNo);

    /**
     * 用过订单的id 获取订单的消息
     * @param orderId:订单的id
     * @return
     */
    Order selOrderByOrderId(@Param("orderId") Integer orderId);
}

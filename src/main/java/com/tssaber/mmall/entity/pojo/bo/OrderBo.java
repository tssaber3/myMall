package com.tssaber.mmall.entity.pojo.bo;

import com.tssaber.mmall.entity.pojo.Goods;
import com.tssaber.mmall.entity.pojo.Order;
import com.tssaber.mmall.entity.pojo.OrderItem;
import com.tssaber.mmall.entity.pojo.dto.OrderDto;

/**
 * @Author:tssaber 订单的Bo 包括order 和 orderItem
 * @Date: 2020/2/1 21:16
 * @Version 1.0
 */
public class OrderBo {

    private Order order;
    private OrderItem orderItem;

    public OrderBo(OrderDto orderDto, Goods goods){
        order = new Order(orderDto);
        orderItem = new OrderItem(goods,orderDto.getOrderNo(),orderDto.getUserId());
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}

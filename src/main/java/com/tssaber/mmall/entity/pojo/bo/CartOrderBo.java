package com.tssaber.mmall.entity.pojo.bo;

import com.tssaber.mmall.entity.pojo.Goods;
import com.tssaber.mmall.entity.pojo.Order;
import com.tssaber.mmall.entity.pojo.OrderItem;
import com.tssaber.mmall.entity.pojo.dto.OrderDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:tssaber 购物车订单的BO 一个order 多个orderItem
 * @Date: 2020/2/9 21:34
 * @Version 1.0
 */
public class CartOrderBo {
    private Order order;
    private List<OrderItem> orderItemList;

    public CartOrderBo(){

    }

    public CartOrderBo(OrderDto orderDto, List<Goods> goodsList){
        this.order = new Order(orderDto);
        this.orderItemList = new ArrayList<>();
        for (Goods goods:goodsList){
            this.orderItemList.add(new OrderItem(goods,orderDto.getOrderNo(),orderDto.getUserId()));
        }
    }
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}

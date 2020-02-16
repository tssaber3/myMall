package com.tssaber.mmall.redis;

/**
 * @Author:tssaber
 * @Date: 2020/2/3 16:49
 * @Version 1.0
 */
public class OrderKey extends BasePrefix {

    private OrderKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    /**
     * 用户的订单前缀 格式 hash prefix + userId作为域 orderNO:order
     */
    public static OrderKey userOrders = new OrderKey(0,"userOrders");

    /**
     * 订单详情
     */
    public static OrderKey orderDetail = new OrderKey(0,"orderDetail");
}

package com.tssaber.mmall.controller.vo;

import com.tssaber.mmall.entity.pojo.Order;
import com.tssaber.mmall.entity.pojo.OrderItem;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/10 15:24
 * @Version 1.0
 */
public class OrderVo {
    private Integer id;
    private Long orderNo;
    private Integer userId;
    private BigDecimal payment;
    private BigDecimal postage;
    private Integer status;
    private String address;
    private String sendTime;
    private List<OrderItem> orderItemList;

    public OrderVo() {
    }

    public OrderVo(Order order){
        DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        this.id = order.getId();
        this.orderNo = order.getOrderNo();
        this.payment = order.getPayment();
        this.postage = order.getPostage();
        this.address = order.getAddress();
        this.status = order.getStatus();
        this.sendTime = sdf.format(new Date(order.getSendTime().getTime()));
        this.orderItemList = order.getOrderItemList();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }

    public Integer getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}

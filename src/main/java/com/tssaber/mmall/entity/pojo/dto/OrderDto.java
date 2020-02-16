package com.tssaber.mmall.entity.pojo.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/2 16:42
 * @Version 1.0
 */
public class OrderDto {

    /**
     * 用户id
     */
    private Integer userId;
    private BigDecimal payment;
    private Integer paymentType;
    private BigDecimal postage;
    private Integer goodId;
    private Long orderNo;
    private String address;
    private List<Integer> goodsIdList;

    public OrderDto() {
    }

    public OrderDto(BigDecimal payment, Integer paymentType, BigDecimal postage,Integer goodId) {
        this.payment = payment;
        this.paymentType = paymentType;
        this.postage = postage;
        this.goodId = goodId;
    }

    public OrderDto(BigDecimal payment,BigDecimal postage,List<Integer> goodsIdList){
        this.payment = payment;
        this.paymentType = 1;
        this.postage = postage;
        this.goodsIdList = goodsIdList;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "userId=" + userId +
                ", payment=" + payment +
                ", paymentType=" + paymentType +
                ", postage=" + postage +
                ", goodId=" + goodId +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getGoodsIdList() {
        return goodsIdList;
    }

    public void setGoodsIdList(List<Integer> goodsIdList) {
        this.goodsIdList = goodsIdList;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }
}

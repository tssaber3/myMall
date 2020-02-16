package com.tssaber.mmall.entity.pojo;

import com.tssaber.mmall.entity.pojo.dto.OrderDto;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description:订单类
 * @author: tssaber
 * @time: 2019/11/24 0024 21:46
 */
@Alias("mmall_order")
@Entity
@Table(name = "mmall_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 订单的编号
     */
    @Column(name = "order_no")
    private Long orderNo;
    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 物流编号
     */
    @Column(name = "shipping_id")
    private Integer shippingId;

    /**
     * 支付金额
     */
    @Column(name = "payment")
    private BigDecimal payment;

    /**
     * 支付方式
     */
    @Column(name = "payment_type")
    private Integer paymentType;

    /**
     * 邮费
     */
    @Column(name = "postage")
    private BigDecimal postage;

    /**
     * 收货地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 订单状态
     */
    @Column(name = "order_status")
    private Integer status;

    /**
     * 支付时间
     */
    @Column(name = "payment_time")
    private Timestamp paymentTime;

    /**
     * 订单创建时间
     */
    @Column(name = "send_time")
    private Timestamp sendTime;

    /**
     * 订单完成时间
     */
    @Column(name = "end_time")
    private Timestamp endTime;

    /**
     * 订单关闭时间
     */
    @Column(name = "close_time")
    private Timestamp closeTime;


    @Column(name = "gmt_create")
    private Timestamp createTime;

    @Column(name = "gmt_modified")
    private Timestamp modifiedTime;

    private transient List<OrderItem> orderItemList;

    public Order() {
    }

    public Order(OrderDto orderDto){
        this.orderNo = orderDto.getOrderNo();
        this.userId = orderDto.getUserId();
        this.payment = orderDto.getPayment();
        this.paymentType = orderDto.getPaymentType();
        this.postage = orderDto.getPostage();
        this.address = orderDto.getAddress();
        this.status = 1;
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.modifiedTime = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNo=" + orderNo +
                ", userId=" + userId +
                ", shippingId=" + shippingId +
                ", payment=" + payment +
                ", paymentType=" + paymentType +
                ", postage=" + postage +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", paymentTime=" + paymentTime +
                ", sendTime=" + sendTime +
                ", endTime=" + endTime +
                ", closeTime=" + closeTime +
                ", createTime=" + createTime +
                ", modifiedTime=" + modifiedTime +
                ", orderItemList=" + orderItemList +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Timestamp closeTime) {
        this.closeTime = closeTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Timestamp modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public BigDecimal getPostage() {
        return postage;
    }

    public void setPostage(BigDecimal postage) {
        this.postage = postage;
    }
}

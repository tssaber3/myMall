package com.tssaber.mmall.entity.pojo;

import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @description:支付信息类
 * @author: tssaber
 * @time: 2019/11/24 0024 22:09
 */
@Alias("mmall_pay_info")
@Entity
@Table(name = "mmall_pay_info")
public class PayInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 订单编号
     */
    @Column(name = "order_no")
    private Long orderNo;

    /**
     * 支付平台
     */
    @Column(name = "pay_platform")
    private Integer payPlatform;

    /**
     * 平台号
     */
    @Column(name = "platform_number")
    private String platformNumber;

    /**
     * 平台状态
     */
    @Column(name = "platform_status")
    private String platformStatus;

    @Column(name = "gmt_create")
    private Timestamp createTime;

    @Column(name = "gmt_modified")
    private Timestamp modifiedTime;

    @Override
    public String toString() {
        return "PayInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderNo=" + orderNo +
                ", payPlatform=" + payPlatform +
                ", platformNumber='" + platformNumber + '\'' +
                ", platformStatus='" + platformStatus + '\'' +
                ", createTime=" + createTime +
                ", modifiedTime=" + modifiedTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getPayPlatform() {
        return payPlatform;
    }

    public void setPayPlatform(Integer payPlatform) {
        this.payPlatform = payPlatform;
    }

    public String getPlatformNumber() {
        return platformNumber;
    }

    public void setPlatformNumber(String platformNumber) {
        this.platformNumber = platformNumber;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
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
}

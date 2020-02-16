package com.tssaber.mmall.entity.pojo;

import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @description:订单明细表
 * @author: tssaber
 * @time: 2019/11/24 0024 22:03
 */
@Alias("mmall_order_item")
@Entity
@Table(name = "mmall_order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    /**
     * order的id
     */
    @Column(name = "order_no")
    private Long orderNo;

    /**
     * 商品的id
     */
    @Column(name = "product_id")
    private Integer productId;

    /**
     * 商品的名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 商品对应的图片
     */
    @Column(name = "product_image")
    private String productImage;

    /**
     * 当前价格
     */
    @Column(name = "current_unit_price")
    private BigDecimal currentPrice;

    /**
     * 商品的个数
     */
    @Column(name = "quantity")
    private Integer quantity;

    /**
     * 商品的总价格
     */
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Timestamp createTime;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Timestamp modifiedTime;

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderNo=" + orderNo +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productImage='" + productImage + '\'' +
                ", currentPrice=" + currentPrice +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", createTime=" + createTime +
                ", modifiedTime=" + modifiedTime +
                '}';
    }

    public OrderItem() {
    }

    public OrderItem(Goods goods,Long orderNo,Integer userId){
        this.userId = userId;
        this.orderNo = orderNo;
        this.productId = goods.getId();
        this.productName = goods.getProductName();
        this.productImage = goods.getMainImage();
        this.quantity = 1;
        this.currentPrice = goods.getPrice();
        this.totalPrice = this.currentPrice;
        this.createTime = new Timestamp(System.currentTimeMillis());
        this.modifiedTime = new Timestamp(System.currentTimeMillis());
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

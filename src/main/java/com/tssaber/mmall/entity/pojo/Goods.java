package com.tssaber.mmall.entity.pojo;

import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/24 0024 22:43
 */
@Alias("mmall_goods")
@Entity
@Table(name = "mmall_goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 类别
     */
    @Column(name = "category_id")
    private Integer categoryId;

    /**
     * 商品名
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 标题
     */
    @Column(name = "sub_title")
    private String subtitle;

    /**
     * 展示图片
     */
    @Column(name = "main_image")
    private String mainImage;

    /**
     * 详情图片
     */
    @Column(name = "sub_images")
    private String subImage;

    /**
     * 细节的文字描述
     */
    @Column(name = "product_detail")
    private String detail;

    /**
     * 价格
     */
    @Column(name = "product_price")
    private BigDecimal price;

    /**
     * 库存
     */
    @Column(name = "product_stock")
    private Integer stock;

    /**
     * 状态
     */
    @Column(name = "product_status")
    private Integer status;

    @Column(name = "gmt_create")
    private Timestamp createTime;

    @Column(name = "gmt_modified")
    private Timestamp modifiedTime;

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", productName='" + productName + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", subImage='" + subImage + '\'' +
                ", detail='" + detail + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", status=" + status +
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

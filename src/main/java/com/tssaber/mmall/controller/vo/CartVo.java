package com.tssaber.mmall.controller.vo;

import com.tssaber.mmall.entity.pojo.Cart;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author:tssaber
 * @Date: 2020/2/7 21:42
 * @Version 1.0
 */
public class CartVo {
    private Integer id;
    private Integer goodsId;
    private String image;
    private String name;
    private BigDecimal price;
    private Integer number;
    private Timestamp createTime;

    public CartVo(){

    }

    public CartVo(Cart cart){
        this.id = cart.getId();
        this.goodsId = cart.getGoodsId();
        this.image = cart.getGoodsImage();
        this.name = cart.getGoodsName();
        this.price = cart.getGoodsPrice();
        this.number = cart.getGoodsNum();
        this.createTime = cart.getCreateTime();
    }

    @Override
    public String toString() {
        return "CartVo{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", number=" + number +
                ", createTime=" + createTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}

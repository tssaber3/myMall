package com.tssaber.mmall.controller.vo;

import com.tssaber.mmall.common.GoodsTypeEnums;
import com.tssaber.mmall.entity.pojo.Goods;

import java.math.BigDecimal;

/**
 * @Author:tssaber 展示给前台的商品对象
 * @Date: 2020/2/3 23:06
 * @Version 1.0
 */
public class GoodsVo {

    /**
     * 商品的id
     */
    private Integer goodsId;

    /**
     * 商品的名字
     */
    private String goodsName;

    /**
     * 商品的类别
     */
    private String goodsType;

    /**
     * 商品的封面图片地址
     */
    private String goodsCoverImg;

    /**
     * 商品的库存
     */
    private Integer goodsStock;

    /**
     * 商品的价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品的详细描述
     */
    private String goodsDetail;

    public GoodsVo() {
    }

    public GoodsVo(Goods goods){
        this.goodsId = goods.getId();
        this.goodsName = goods.getProductName();
        this.goodsType = GoodsTypeEnums.valueOf(goods.getCategoryId()).getType();
        this.goodsCoverImg = goods.getMainImage();
        this.goodsStock = goods.getStock();
        this.goodsPrice = goods.getPrice();
        this.goodsDetail = goods.getDetail();
    }

    @Override
    public String toString() {
        return "GoodsVo{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", goodsCoverImg='" + goodsCoverImg + '\'' +
                ", goodsStock=" + goodsStock +
                ", goodsPrice=" + goodsPrice +
                ", goodsDetail='" + goodsDetail + '\'' +
                '}';
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCoverImg() {
        return goodsCoverImg;
    }

    public void setGoodsCoverImg(String goodsCoverImg) {
        this.goodsCoverImg = goodsCoverImg;
    }

    public Integer getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(Integer goodsStock) {
        this.goodsStock = goodsStock;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
}

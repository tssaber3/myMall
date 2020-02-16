package com.tssaber.mmall.entity.pojo.dto;

import com.tssaber.mmall.controller.vo.CartVo;
import com.tssaber.mmall.controller.vo.GoodsVo;
import com.tssaber.mmall.entity.pojo.Cart;

import java.util.List;

/**
 * @Author:tssaber 用于购物车结算时的对象
 * @Date: 2020/2/8 10:51
 * @Version 1.0
 */
public class CartBo {

    private List<Integer> goodsIdList;
    private String md5Url;
    private String address;
    private List<GoodsVo> goodsVos;

    public CartBo(List<Integer> goodsIdList, String md5Url) {
        this.goodsIdList = goodsIdList;
        this.md5Url = md5Url;
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

    public String getMd5Url() {
        return md5Url;
    }

    public void setMd5Url(String md5Url) {
        this.md5Url = md5Url;
    }

    public List<GoodsVo> getGoodsVos() {
        return goodsVos;
    }

    public void setGoodsVos(List<GoodsVo> goodsVos) {
        this.goodsVos = goodsVos;
    }
}

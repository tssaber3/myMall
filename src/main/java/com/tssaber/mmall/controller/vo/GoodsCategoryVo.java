package com.tssaber.mmall.controller.vo;

import com.tssaber.mmall.common.GoodsTypeEnums;
import com.tssaber.mmall.entity.pojo.Goods;

import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/16 0:46
 * @Version 1.0
 */
public class GoodsCategoryVo {
    private List<Goods> goods;
    private Integer pageNum;
    private String category;

    public GoodsCategoryVo() {
    }

    public GoodsCategoryVo(List<Goods> goods){
        this.goods = goods;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

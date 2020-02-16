package com.tssaber.mmall.controller.vo;

import com.tssaber.mmall.entity.pojo.Goods;
import com.tssaber.mmall.entity.pojo.dto.UserDto;

import java.math.BigDecimal;

/**
 * @Author:tssaber 用于客户端请求加密的url的
 * @Date: 2020/2/11 11:25
 * @Version 1.0
 */
public class UrlVo {
    private String md5;
    private Goods goods;
    private String address;

    public UrlVo() {
    }

    public UrlVo(String md5, Goods goods, UserDto userDto){
        this.md5 = md5;
        this.goods = goods;
        this.address = userDto.getAddress();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}

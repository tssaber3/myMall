package com.tssaber.mmall.service.Impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.dao.CartMapper;
import com.tssaber.mmall.entity.pojo.Cart;
import com.tssaber.mmall.entity.pojo.Goods;
import com.tssaber.mmall.entity.pojo.User;
import com.tssaber.mmall.entity.pojo.bo.OrderBo;
import com.tssaber.mmall.entity.pojo.dto.CartBo;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.redis.CartKey;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.redis.SeckillKey;
import com.tssaber.mmall.service.CartService;
import com.tssaber.mmall.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/7 14:21
 * @Version 1.0
 */
@Service
public class CartServiceImpl implements CartService {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private GoodsServiceImpl goodsService;

    @Resource
    private CartMapper cartMapper;

    @Resource
    private RedisUtils redisUtils;

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addCart(String cartInfo, HttpServletRequest request) {
        Goods goods = null;
        Cart cart = null;
        try {
            JsonObject object = new JsonParser().parse(cartInfo).getAsJsonObject();
            goods = goodsService.selGoodsById(object.get("goodsId").getAsInt());
            cart = new Cart(goods,object.get("goodsNum").getAsInt());
        }catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        UserDto user = userService.selUser(request);
        cart.setUserId(user.getId());
        boolean bok = cartMapper.insCart(cart);
        if (bok){
            try {
                redisUtils.tryLock(CartKey.cartLock,"selCartInfo",String.valueOf(Thread.currentThread()));
                redisUtils.rPush(CartKey.cartInfo,String.valueOf(user.getId()),redisUtils.beanToString(cart));
            }finally {
                redisUtils.unLock(CartKey.cartLock,"sekCartInfo",String.valueOf(Thread.currentThread().getId()));
            }
        }
        return bok;
    }

    @Override
    public List<Cart> selCartAll(HttpServletRequest request) {
        UserDto userDto = userService.selUser(request);
        List<Cart> carts = redisUtils.lRange(CartKey.cartInfo,String.valueOf(userDto.getId()),0,-1,Cart.class);
        if (carts != null){
            log.info("缓存中存在");
            return carts;
        }
        log.info("缓存中不存在到数据库中去查询");
        carts = cartMapper.selCartByUserId(userDto.getId());
        if (!carts.isEmpty()){
            try {
                redisUtils.tryLock(CartKey.cartLock,"selCartInfo",String.valueOf(Thread.currentThread().getId()));
                redisUtils.clear(CartKey.cartInfo,String.valueOf(userDto.getId()));
                for (Cart cart:carts){
                    redisUtils.rPush(CartKey.cartInfo,String.valueOf(userDto.getId()),redisUtils.beanToString(cart));
                }
            }finally {
                redisUtils.unLock(CartKey.cartLock,"selCartInfo",String.valueOf(Thread.currentThread().getId()));
            }
            return carts;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delCartById(HttpServletRequest request, Integer cartId) {
        UserDto userDto = userService.selUser(request);
        if (userDto == null){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        //判断cartsId是不是在redis和数据库中 用布隆过滤器
        //先删除数据库 再删除缓存
        cartMapper.delCartById(cartId);
        redisUtils.clear(CartKey.cartInfo,String.valueOf(userDto.getId()));
        return true;
    }

    /**
     * 1.解析orderInfo字符串
     * 2.判断goodsId对应的商品是否有库存
     * 3.将所有商品的id加在一起生成加密的url
     * @param orderInfo
     * @return
     */
    @Override
    public CartBo exportUrl(String orderInfo) {
        List<Integer> goodsIdList = new ArrayList<>();
        //解析orderInfo字符串
        try {
            JsonObject object = new JsonParser().parse(orderInfo).getAsJsonObject();
            JsonArray array = object.getAsJsonArray("goodsId");
            for (int i = 0;i < array.size();i++){
                JsonObject jsonObject = (JsonObject) array.get(i);
                goodsIdList.add(jsonObject.get("goodsId").getAsInt());
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        if (goodsIdList.size() == 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        boolean exists = false;
        StringBuilder sb = new StringBuilder();
        for (Integer integer:goodsIdList){
            exists = redisUtils.exists(SeckillKey.isGoodsOver,String.valueOf(integer));
            if (!exists){
                throw new GlobalException(CExceptionEnums.SECKILL_OVER);
            }
            sb.append(integer);
        }
        return new CartBo(goodsIdList,UrlUtils.getKuaishouSign(sb.toString()));
    }

    @Override
    public void delCartAll(Integer userId) {
        cartMapper.delCartByUserId(userId);
        redisUtils.clear(CartKey.cartInfo,String.valueOf(userId));
    }

}

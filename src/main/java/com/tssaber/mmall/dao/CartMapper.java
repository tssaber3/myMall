package com.tssaber.mmall.dao;

import com.tssaber.mmall.entity.pojo.Cart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/7 14:00
 * @Version 1.0
 */
@Repository
public interface CartMapper {

    /**
     * 插入购物车商品
     * @param cart
     * @return
     */
    boolean insCart(Cart cart);

    /**
     * 通过用户名获取购物车的所有内容
     * @param userId:用户id
     * @return
     */
    List<Cart> selCartByUserId(@Param("userId") Integer userId);

    /**
     * 删除购物车中的内容
     * @param cartId:对应的购物车id
     * @return
     */
    boolean delCartById(@Param("cartId") Integer cartId);

    /**
     * 删除用户的购物车所有内容
     * @param userId
     * @return
     */
    boolean delCartByUserId(@Param("userId") Integer userId);
}

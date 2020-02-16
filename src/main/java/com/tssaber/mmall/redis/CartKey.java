package com.tssaber.mmall.redis;

/**
 * @Author:tssaber
 * @Date: 2020/2/7 14:19
 * @Version 1.0
 */
public class CartKey extends BasePrefix {

    private static final  int CART_INFO_EXPIRE =3600 * 24 * 2;

    private CartKey(int expireSeconds, String prefix){
        super(expireSeconds,prefix);
    }

    /**
     * 购物车信息
     */
    public static CartKey cartInfo = new CartKey(CART_INFO_EXPIRE,"cartInfo");

    /**
     * 购物车的锁
     */
    public static CartKey cartLock = new CartKey(1000,"cartLock");
}

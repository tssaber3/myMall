package com.tssaber.mmall.redis;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/12/6 0006 19:06
 */
public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds, String prefix){
        super(expireSeconds,prefix);
    }

    /**
     * 商品列表缓存时的前缀 时间为 120秒
     */
    public static GoodsKey getGoodsList = new GoodsKey(120,"goodsList");

    /**
     * 商品详情前缀 缓存时间为 120秒
     */
    public static GoodsKey getGoodsDetails = new GoodsKey(120,"goodsDetails");

    /**
     * 商品库存的前缀 缓存时间不过期
     */
    public static GoodsKey getGoodsStock = new GoodsKey(1000000,"goodsStock");

    /**
     * 商品的锁的前缀
     */
    public static GoodsKey goodsLock = new GoodsKey(100,"goodsLock");

    /**
     * 最新100个的商品前缀 每次插入时都要更新
     */
    public static GoodsKey goodsNew = new GoodsKey(0,"goodsNew");

    /**
     * 获取商品信息的锁
     */
    public static GoodsKey selGoods = new GoodsKey(100,"selGoods");

    /**
     * 根据商品类型获取商品列表
     */
    public static GoodsKey GoodsCategory = new GoodsKey(0,"goodsCategory");
}

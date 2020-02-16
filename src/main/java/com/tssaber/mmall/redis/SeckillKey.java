package com.tssaber.mmall.redis;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/12/6 0006 19:15
 */
public class SeckillKey extends BasePrefix {

    private SeckillKey(String prefix){
        super(prefix);
    }

    /**
     * 商品是否销售完
     */
    public static SeckillKey isGoodsOver = new SeckillKey("exit");
}

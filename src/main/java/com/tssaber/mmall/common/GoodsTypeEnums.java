package com.tssaber.mmall.common;

/**
 * @Author:tssaber 商品类别的枚举
 * @Date: 2020/2/3 23:12
 * @Version 1.0
 */
public enum  GoodsTypeEnums {
    /**
     * 家电 数码 手机
     */
    GOODS_TYPE_1("家电 数码 手机"),

    /**
     * 女装 男装 穿搭
     */
    GOODS_TYPE_2("女装 男装 穿搭"),

    /**
     * 女装 男装 穿搭
     */
    GOODS_TYPE_3("女装 男装 穿搭"),

    /**
     * 运动 户外 乐器
     */
    GOODS_TYPE_4("运动 户外 乐器"),

    /**
     * 游戏 动漫 影视
     */
    GOODS_TYPE_5("游戏 动漫 影视"),

    /**
     * 美妆 清洁 宠物
     */
    GOODS_TYPE_6("美妆 清洁 宠物"),

    /**
     * 工具 装修 建材
     */
    GOODS_TYPE_7("工具 装修 建材"),

    /**
     * 鞋靴 箱包 配件
     */
    GOODS_TYPE_8("鞋靴 箱包 配件"),

    /**
     * 玩具 孕产 用品
     */
    GOODS_TYPE_9("玩具 孕产 用品"),

    GOODS_TYPE_NULL("空");
    ;

    private String type;

    GoodsTypeEnums(String type){
        this.type = type;
    }

    public static GoodsTypeEnums valueOf(int type){
        switch (type){
            case 1:
                return GOODS_TYPE_1;
            case 2:
                return GOODS_TYPE_2;
            case 3:
                return GOODS_TYPE_3;
            case 4:
                return GOODS_TYPE_4;
            case 5:
                return GOODS_TYPE_5;
            case 6:
                return GOODS_TYPE_6;
            case 7:
                return GOODS_TYPE_7;
            case 8:
                return GOODS_TYPE_8;
            case 9:
                return GOODS_TYPE_9;
            default:return GOODS_TYPE_NULL;
        }
    }

    public String getType() {
        return type;
    }
}

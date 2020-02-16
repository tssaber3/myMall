package com.tssaber.mmall.common;


/**
 * description:异常枚举类
 * author: tssaber
 * time: 2019/11/24 0024 21:46
 */
public enum CExceptionEnums {
    /**
     * 通用异常码
     */
    SERVER_ERROR("500100","服务端异常"),
    BIND_ERROR("500101","参数校验异常"),
    ACCESS_LIMIT_REACHED("500104","访问高峰期,请稍等"),

    /**
     * 登录模块
     */
    SESSION_ERROR("500210","Session不存在或者已经失效"),
    PASSWORD_EMPTY("500211","登录密码不能为空"),
    MOBILE_EMPTY("500212","手机号不能为空"),
    MOBILE_ERROR("500213","手机号格式错误"),
    MOBILE_NOT_EXIST("500214","手机号不存在"),
    PASSWORD_ERROR("500215","密码错误"),
    PRIMARY_ERROR("500216","主键冲突"),
    USERNAME_EMPTY("500217","用户名不能为空"),
    USER_NOT_EXIST("500218","用户不存在"),
    TOKEN_ERROR("500219","Token失效或不存在"),
    API_OVER_USE("500220","api访问多次"),

    /**
     * 订单模块
     */
    ORDER_NOT_EXIST("500400","订单不存在"),

    /**
     * 商品销售模块
     */
    SECKILL_OVER("500500","商品销售完毕"),
    REPEATE_SECKILL("500501","限定商品无法重复购买"),
    SHOP_TOKEN_ERROR("500502","接口防刷的token失效"),

    /**
     * 购物车模块
     */
    CART_NULL("500600","购物车为空")
    ;



    private final String errorCode;

    private final String errorDesc;

    CExceptionEnums(String errorCode,String errorDesc){
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
}

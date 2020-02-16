package com.tssaber.mmall.redis;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/26 0026 14:46
 */
public class UserKey extends BasePrefix {

    /**
     * token默认保存两天
     */
    private static final  int TOKEN_EXPIRE =3600 * 24 * 2;

    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 存储token时的前缀并且同时定义过期时间2天
     */
    public static UserKey token = new UserKey(TOKEN_EXPIRE, "token");

    /**
     * 用户信息前缀
     */
    public static UserKey userInfo = new UserKey(0,"userInfo");


    /**
     * redis缓存用户时调用 过期时间设置为0 表示永不过期
     */
    public static UserKey userIdPrefix = new UserKey(0,"id");

    /**
     * 防刷api 存储的是每秒访问的次数 apiPrefix + username : count(Integer)
     */
    public static UserKey apiPrefix = new UserKey(1,"api");
}

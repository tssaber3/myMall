package com.tssaber.mmall.redis;

/**
 * @description: 实现前缀接口的基础前缀类
 * @author: tssaber
 * @time: 2019/11/26 0026 14:41
 */
public abstract class BasePrefix implements KeyPrefix{

    /**
     * 过期时间
     */
    private int expireSeconds;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 设置前缀并且设置过期时间
     * @param expireSeconds
     * @param prefix
     */
    public BasePrefix(int expireSeconds,String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    /**
     * 设置前缀 没有过期时间 是持久化节点
     * @param prefix
     */
    public BasePrefix(String prefix){
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + prefix;
    }
}

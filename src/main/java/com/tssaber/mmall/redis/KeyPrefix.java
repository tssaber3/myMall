package com.tssaber.mmall.redis;


/**
 * @description: 缓冲前缀的公有接口
 * @author: tssaber
 * @time: 2019/11/25 0025 19:27
 */
public interface KeyPrefix {

    /**
     * 有效期
     * @return
     */
    int expireSeconds();


    /**
     * 获取前缀
     * @return
     */
    String getPrefix();
}

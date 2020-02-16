package com.tssaber.mmall.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author:tssaber 秒杀部分的接口
 * @Date: 2020/2/1 20:09
 * @Version 1.0
 */
@Service
public interface SeckillService {

    /**
     * 用户下订单时要请求url 查询redis 中商品是否卖完
     * 卖完了就返回null
     * 没到时间也返回null
     * 返回加密的url
     * @param goodId :商品的id
     * @return
     */
    String exportSeckillUrl(String goodId);

    /**
     * 用户使用加了密的url请求订单
     * 该接口用来对加密部分进行解析查看是否能进行解密
     * 创建订单 并且对商品储存进行减少
     * @param md5：url的加密部分
     * @param goodId 商品的id
     * @param request 用来获取用户信息
     * @param orderInfo order需要的信息
     * @return 订单成功
     */
    Boolean executionSeckillId(String md5, Integer goodId, String orderInfo,HttpServletRequest request);

    /**
     * 用于秒杀时扣除库存(在redis中)之后通过MQ发送消息进行落库
     * @param goodsId
     * @return
     */
    long stock(Integer goodsId);
}

package com.tssaber.mmall.service;

import com.tssaber.mmall.controller.vo.GoodsCategoryVo;
import com.tssaber.mmall.controller.vo.GoodsVo;
import com.tssaber.mmall.entity.pojo.Goods;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:tssaber 商品的服务(product)
 * @Date: 2020/2/1 21:12
 * @Version 1.0
 */
@Service
public interface GoodsService {

    /**
     * 通过商品的id获取商品的信息
     * @param id
     * @return
     */
    Goods selGoodsById(Integer id);

    /**
     * 通过商品的id获得商品的库存
     * @param goodsId
     * @return
     */
    Integer getStock(Integer goodsId);

    /**
     * 减少商品库存
     * @param goodId:商品的id
     * @param count:扣除库存的数量
     * @return
     */
    boolean goodsReduction(Integer goodId,Integer count);

    /**
     * 得到最新的3个商品
     * @return:
     */
    List<GoodsVo> selGoodsNew();

    /**
     * 随机选取10件商品用于主业展示
     * @return
     */
    List<Goods> selGoodsRand();

    /**
     * 获取全部的商品
     * @return
     */
    List<Goods> selGoodsAll();

    /**
     * 根据商品的id获取list
     * @param goodsIdList
     * @return
     */
    List<Goods> selAllGoodsById(List<Integer> goodsIdList);

    /**
     * 将有库存的商品加到redis中
     * @return
     */
    void isGoodsOver();

    /**
     * 根据商品类型获取商品list
     * @param category
     * @param page
     * @return
     */
    GoodsCategoryVo selGoodsByCategory(Integer category, Integer page);
}

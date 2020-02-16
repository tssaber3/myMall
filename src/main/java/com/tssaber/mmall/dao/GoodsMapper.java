package com.tssaber.mmall.dao;

import com.tssaber.mmall.entity.pojo.Goods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/1 21:21
 * @Version 1.0
 */
@Repository
public interface GoodsMapper {

    /**
     * 通过商品的id获取商品的详细信息
     * @param goodsId:商品id
     * @return
     */
    Goods selGoodsById(@Param("goodsId") int goodsId);

    /**
     * 通过商品的id获取库存
     * @param goodsId
     * @return
     */
    Integer selStockById(@Param("goodsId") Integer goodsId);

    /**
     * 商品库存减少1
     * @param goodId
     * @return
     */
    boolean reductionGoods(@Param("goodsId")Integer goodId);

    /**
     * 插入商品
     * @param goods
     * @return
     */
    boolean insGoods(Goods goods);

    /**
     * 选出最新的100个商品
     * @return
     */
    List<Goods> selGoodsNew();

    /**
     * 根据类别选择商品
     * @param category:类别的id
     * @return
     */
    List<Goods> selGoodsByCategory(@Param("category") Integer category);
}

package com.tssaber.mmall.service.Impl;

import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.common.GoodsTypeEnums;
import com.tssaber.mmall.controller.vo.GoodsCategoryVo;
import com.tssaber.mmall.controller.vo.GoodsVo;
import com.tssaber.mmall.dao.GoodsMapper;
import com.tssaber.mmall.entity.pojo.Goods;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.redis.GoodsKey;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.redis.SeckillKey;
import com.tssaber.mmall.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author:tssaber
 * @Date: 2020/2/1 21:20
 * @Version 1.0
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private GoodsMapper goodsMapper;

    private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    private static final Integer PAGE_NUMBER = 10;

    /**
     * 1.先从redis中查找
     * 2.没有再到数据库中进行查询
     * 3.之后将信息保存到redis中
     * @param id
     * @return
     */
    @Override
    public Goods selGoodsById(Integer id) {
        List<Goods> goods = redisUtils.hget(GoodsKey.getGoodsDetails,"goodsDetail",Goods.class,String.valueOf(id));
        if (goods != null && goods.size() != 0 && goods.get(0) != null){
            log.info("缓存中存在对应的商品信息");
            return goods.get(0);
        }
        log.info("缓存中不存在 到数据库中进行查询");
        Goods goods1 = goodsMapper.selGoodsById(id);
        if (goods1 != null){
            redisUtils.hset(GoodsKey.getGoodsDetails,"goodsDetail",String.valueOf(id),goods1);
        }
        return goods1;
    }

    @Override
    public Integer getStock(Integer goodsId) {
        return goodsMapper.selStockById(goodsId);
    }

    @Override
    public boolean goodsReduction(Integer goodId, Integer count) {
        return goodsMapper.reductionGoods(goodId);
    }

    /**
     * 返回最新的三个商品给首页页面
     * @return
     */
    @Override
    public List<GoodsVo> selGoodsNew() {
        List<GoodsVo> goodsVoList = new ArrayList<>();
        List<Goods> goods = redisUtils.lRange(GoodsKey.goodsNew,"goodsNew",0,4,Goods.class);
        if (goods != null){
            log.info("缓存中存在 从缓存中取");
            for (Goods goods1 : goods){
                goodsVoList.add(new GoodsVo(goods1));
            }
            return goodsVoList;
        }
        log.info("缓存中不存在 到数据库中查询");
        goods = selGoodsAll();
        if (goods.size() != 0){
            for (int i = 0;i < 5;i++){
                goodsVoList.add(new GoodsVo(goods.get(i)));
            }
            return goodsVoList;
        }
        return null;
    }

    @Override
    public List<Goods> selGoodsRand() {
        List<Goods> result = new ArrayList<>();
        List<Goods> goods = redisUtils.lRange(GoodsKey.goodsNew,"goodsNew",0,-1,Goods.class);
        if (goods != null && goods.size() > 10){
            result = randGetGoods(goods,10);
            return result;
        }
        log.info("缓存中不存在  到数据库中查询");
        goods = selGoodsAll();
        if (goods.size() != 0){
            return goods;
        }
        return null;
    }

    /**
     * 获取全部的商品
     * @return
     */
    @Override
    public List<Goods> selGoodsAll() {
        List<Goods> goods = redisUtils.lRange(GoodsKey.goodsNew,"goodsNew",0,-1,Goods.class);
        if (goods != null){
            return goods;
        }
        log.info("缓存中不存在到 数据库中查询");
        goods = goodsMapper.selGoodsNew();
        if (goods.size() != 0){
            try {
                redisUtils.tryLock(GoodsKey.selGoods,"selGoods",String.valueOf(Thread.currentThread().getId()));
                redisUtils.clear(GoodsKey.goodsNew,"goodsNew");
                for (int i = 0;i < goods.size();i++){
                    redisUtils.rPush(GoodsKey.goodsNew,"goodsNew",redisUtils.beanToString(goods.get(i)));
                }
            }finally {
                redisUtils.unLock(GoodsKey.selGoods,"selGoods",String.valueOf(Thread.currentThread().getId()));
            }
            return goods;
        }
        return null;
    }

    @Override
    public List<Goods> selAllGoodsById(List<Integer> goodsIdList) {
        String[] strings = listToStrings(goodsIdList);
        List<Goods> goods = redisUtils.hget(GoodsKey.getGoodsDetails,"goodsDetail",Goods.class,strings);
        if (goods != null && goods.get(0) != null){
            log.info("缓存中存在 从缓存中拿取");
            return goods;
        }
        log.info("缓存中不存在 到数据库中进行查询");
        goods = new ArrayList<>();
        for (Integer integer:goodsIdList){
            goods.add(selGoodsById(integer));
        }
        return goods;
    }

    @Override
    public void isGoodsOver() {
        List<Goods> goods = selGoodsAll();
        for (Goods goods1:goods){
            redisUtils.set(SeckillKey.isGoodsOver,String.valueOf(goods1.getId()),String.valueOf(goods1.getId()));
        }
    }

    @Override
    public GoodsCategoryVo selGoodsByCategory(Integer category, Integer page) {
        GoodsCategoryVo goodsCategoryVo = new GoodsCategoryVo();
        List<Goods> goods = redisUtils.lRange(GoodsKey.GoodsCategory,String.valueOf(category),(page - 1) * 10,page * 10 - 1,Goods.class);
        long size = redisUtils.lsize(GoodsKey.GoodsCategory,String.valueOf(category));
        if (goods != null && size != 0){
            goodsCategoryVo.setGoods(goods);
            goodsCategoryVo.setPageNum(Math.toIntExact(size));
            goodsCategoryVo.setCategory(GoodsTypeEnums.valueOf(category).getType());
            return goodsCategoryVo;
        }
        log.info("缓存中不存在到 数据库中查询");
        goods = goodsMapper.selGoodsByCategory(category);
        System.out.println(goods.get(0));
        if (goods.size() != 0){
            try {
                redisUtils.tryLock(GoodsKey.selGoods,"selGoodsByCategory",String.valueOf(Thread.currentThread().getId()));
                for (Goods good : goods) {
                    redisUtils.rPush(GoodsKey.GoodsCategory, String.valueOf(category), redisUtils.beanToString(good));
                }
            }finally {
                redisUtils.unLock(GoodsKey.selGoods,"selGoodsByCategory",String.valueOf(Thread.currentThread().getId()));
            }
            if ((page - 1) * PAGE_NUMBER > goods.size()){
                log.info("页面超过最大值");
                throw new GlobalException(CExceptionEnums.BIND_ERROR);
            }
            List<Goods> newGoods;
            if (page * PAGE_NUMBER > goods.size()){
                newGoods = goods.subList((page - 1) * 10,goods.size());
            }else {
                newGoods = goods.subList((page - 1) * 10,page * PAGE_NUMBER);
            }
            System.out.println(newGoods);
            goodsCategoryVo.setGoods(newGoods);
            goodsCategoryVo.setPageNum(goods.size());
            goodsCategoryVo.setCategory(GoodsTypeEnums.valueOf(category).getType());
            return goodsCategoryVo;
        }
        return null;
    }

    private String[] listToStrings(List<Integer> list){
        if (list.size() == 0){
            return null;
        }
        String[] strings = new String[list.size()];
        for (int i = 0;i < list.size();i++){
            strings[i] = String.valueOf(list.get(i));
        }
        return strings;
    }

    /**
     * 从list随机获取n件商品
     * @param goods:商品list
     * @param count:获取的件数
     * @return
     */
    private List<Goods> randGetGoods(List<Goods> goods,int count){
        if (goods.size() < 10){
            return null;
        }
        List<Goods> goodsList = new ArrayList<>();
        Random random = new Random();
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0;i < count;i++){
            int j = random.nextInt(goods.size());
            //判断是否重复
            if (!integerList.contains(j)){
                integerList.add(j);
                goodsList.add(goods.get(j));
            }else {
                //重复就再来一次
                i --;
            }
        }
        return goodsList;
    }
}

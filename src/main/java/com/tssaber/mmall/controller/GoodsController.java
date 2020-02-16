package com.tssaber.mmall.controller;

import com.google.gson.Gson;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.common.GoodsTypeEnums;
import com.tssaber.mmall.controller.vo.GoodsCategoryVo;
import com.tssaber.mmall.controller.vo.GoodsVo;
import com.tssaber.mmall.entity.pojo.Goods;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.service.Impl.GoodsServiceImpl;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/3 23:01
 * @Version 1.0
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    private static final Logger log = LoggerFactory.getLogger(GoodsController.class);

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    @Resource
    private GoodsServiceImpl goodsService;

    /**
     * 查看商品详情的接口
     * @param request
     * @param response
     * @param goodsId
     * @return
     */
    @RequestMapping("/{goodsId}/getGoodsDetail")
    public String getGoodsDetail(HttpServletRequest request, HttpServletResponse response,
                                 @PathVariable("goodsId") Integer goodsId){
        if (goodsId < 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        Goods goods = goodsService.selGoodsById(goodsId);
        GoodsVo goodsVo = new GoodsVo(goods);
        return GSON.toJson(ApiResponse.success(goodsVo));
    }

    /**
     * 获得最新的三个商品
     * @return
     */
    @GetMapping("/getGoodsNew")
    public String getGoodsNew(){
        List<GoodsVo> goodsVoList = goodsService.selGoodsNew();
        if (goodsVoList == null){
            throw new GlobalException(CExceptionEnums.SERVER_ERROR);
        }
        return GSON.toJson(ApiResponse.success(goodsVoList));
    }

    /**
     * 随机10件获取商品
     * @return
     */
    @GetMapping("/getGoodsRand")
    public String getGoodsRand(){
        List<Goods> goods = goodsService.selGoodsRand();
        if (goods == null){
            throw new GlobalException(CExceptionEnums.SERVER_ERROR);
        }
        List<GoodsVo> goodsVoList = new ArrayList<>();
        for (Goods goods1:goods){
            goodsVoList.add(new GoodsVo(goods1));
        }
        return GSON.toJson(ApiResponse.success(goodsVoList));
    }

    /**
     * 根据类别获取商品
     * @param request
     * @param response
     * @param category:商品类别对应的类别数字
     * @return
     */
    @GetMapping("/{category}/{page}/getGoodsByCategory")
    public String getGoodsByCategory(HttpServletRequest request,HttpServletResponse response,
                                     @PathVariable("category") Integer category,
                                     @PathVariable("page") Integer page){
        if (GoodsTypeEnums.valueOf(category) == GoodsTypeEnums.GOODS_TYPE_NULL || page <= 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        GoodsCategoryVo goodsCategoryVo = goodsService.selGoodsByCategory(category,page);
        if (goodsCategoryVo != null){
            return GSON.toJson(ApiResponse.success(goodsCategoryVo));
        }
        throw new GlobalException(CExceptionEnums.SERVER_ERROR);
    }
}

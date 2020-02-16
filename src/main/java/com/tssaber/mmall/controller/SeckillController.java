package com.tssaber.mmall.controller;

import com.google.gson.Gson;
import com.tssaber.mmall.annotation.UserLoginToken;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.controller.vo.UrlVo;
import com.tssaber.mmall.entity.pojo.Goods;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.service.Impl.GoodsServiceImpl;
import com.tssaber.mmall.service.Impl.SeckillServiceImpl;
import com.tssaber.mmall.service.Impl.UserServiceImpl;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:tssaber 秒杀相关接口
 * @Date: 2020/2/1 18:46
 * @Version 1.0
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Resource
    private SeckillServiceImpl seckillService;

    @Resource
    private GoodsServiceImpl goodService;

    @Resource
    private UserServiceImpl userService;

    private static final Logger log = LoggerFactory.getLogger(SeckillController.class);

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    /**
     * 通过商品的id 获得加了密的部分url 和商品的信息 这是进入订单详情时 并没有扣库存
     * @param request
     * @param response
     * @param goodId:商品id
     * @return
     */
    @UserLoginToken
    @RequestMapping("/{goodId}/getUrl")
    public String getUrl(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable("goodId") Integer goodId){
        String md5 = seckillService.exportSeckillUrl(String.valueOf(goodId));
        Goods goods = goodService.selGoodsById(goodId);
        UserDto userDto = userService.selUser(request);
        if (goods != null && md5 != null && md5.length() != 0){
            UrlVo urlVo = new UrlVo(md5,goods,userDto);
            return GSON.toJson(ApiResponse.success(urlVo));
        }
        return GSON.toJson(ApiResponse.error(CExceptionEnums.BIND_ERROR));
    }


    /**
     * 确认订单 支付的时候 扣库存 同时将订单信息入库
     * 这里可以放一个布隆过滤器 来过滤goodId
     * 在上架商品的时候往布隆过滤器中put
     * @param request
     * @param response
     * @param goodsId
     * @param md5
     * @param orderInfo:订单需要的数据 json格式
     * @return
     */
    @UserLoginToken
    @RequestMapping(value = "/{goodSId}/{md5}/confirmOrder")
    public String confirmOrder(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable("goodSId") Integer goodsId,
                               @PathVariable("md5")String md5,
                               @RequestParam("orderInfo")String orderInfo){
        if (orderInfo == null || orderInfo.length() == 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        boolean bok = seckillService.executionSeckillId(md5,goodsId,orderInfo,request);
        if (bok){
            return GSON.toJson(ApiResponse.success("下单成功"));
        }
        return GSON.toJson(ApiResponse.error(CExceptionEnums.BIND_ERROR));
    }
}

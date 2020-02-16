package com.tssaber.mmall.controller;

import com.google.gson.Gson;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.service.Impl.CartServiceImpl;
import com.tssaber.mmall.service.Impl.OrderServiceImpl;
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

/**
 * @Author:tssaber
 * @Date: 2020/2/9 15:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderServiceImpl orderService;

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    @RequestMapping("/{md5}/confirmOrder")
    public String confirmOrder(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam("orderInfo") String orderInfo,
                               @PathVariable("md5") String md5){
        if (orderInfo == null ||orderInfo.length() == 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        boolean bok = orderService.confirmOrder(md5,orderInfo,request);
        if (bok){
            return GSON.toJson(ApiResponse.success("成功"));
        }
        throw new GlobalException(CExceptionEnums.BIND_ERROR);
    }
}

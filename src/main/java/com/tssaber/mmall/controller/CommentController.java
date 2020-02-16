package com.tssaber.mmall.controller;

import com.google.gson.Gson;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.dao.UserMapper;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.service.UserService;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:tssaber
 * @Date: 2020/1/29 21:37
 * @Version 1.0
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    /**
     * 传入的只有手机号和密码
     * @param response
     * @param request
     * @param userInfo
     * @return
     */
    @RequestMapping("/register")
    public String register(HttpServletResponse response, HttpServletRequest request,
                           @RequestParam("userInfo") String userInfo){
        if (userInfo == null || userInfo.length() == 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        String token = userService.addUser(userInfo);
        if (token != null && token.length() != 0){
            return GSON.toJson(ApiResponse.success(token));
        }else {
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
    }
}

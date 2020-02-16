package com.tssaber.mmall.controller;

import com.google.gson.Gson;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/25 0025 19:12
 */
@RestController
@RequestMapping("/error")
public class ErrorController {

    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    private static Gson gson = CommentUtil.INSTANCE.getGson();

    @RequestMapping("/text")
    public String text(){
        throw new GlobalException(CExceptionEnums.ACCESS_LIMIT_REACHED);
    }
}

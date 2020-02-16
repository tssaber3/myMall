package com.tssaber.mmall.interceptor;

import com.google.gson.Gson;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/25 0025 18:43
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionInterceptor.class);

    private Gson gson = CommentUtil.INSTANCE.getGson();

    @ExceptionHandler(value = Exception.class)
    public String globalExceptionHandler(Exception e){
        if (e instanceof GlobalException){
            log.info("GlobalException相关异常");
            GlobalException ex = (GlobalException) e;
            return gson.toJson(ApiResponse.error(ex.getcExceptionEnums()));
        }else {
            return gson.toJson(ApiResponse.error(CExceptionEnums.SERVER_ERROR));
        }
    }
}

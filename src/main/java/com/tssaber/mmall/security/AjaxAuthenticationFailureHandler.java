package com.tssaber.mmall.security;

import com.google.gson.Gson;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description:
 * @author: tssaber
 * @time: 2020/1/10 0010 17:27
 */
@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(AjaxAuthenticationFailureHandler.class);

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    /**
     * @param httpServletRequest
     * @param response
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.info("进入登录失败拦截器");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/xml;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Headers", "token");
        PrintWriter out = response.getWriter();
        out.print(GSON.toJson(ApiResponse.error(CExceptionEnums.PASSWORD_EMPTY)));
        out.flush();
        out.close();
    }
}

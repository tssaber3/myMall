package com.tssaber.mmall.security;

import com.google.gson.Gson;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.redis.UserKey;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:tssaber 退出成功拦截器
 * @Date: 2020/2/15 22:16
 * @Version 1.0
 */
@Component
public class AuthenticationLoginOutSuccessHandler implements LogoutSuccessHandler {


    private static final Logger log = LoggerFactory.getLogger(AuthenticationLoginOutSuccessHandler.class);

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("进入退出成功拦截器");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setContentType("text/xml;charset=UTF-8");
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "token");
        httpServletResponse.getWriter().print(GSON.toJson(ApiResponse.success("退出成功")));
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }
}

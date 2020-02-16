package com.tssaber.mmall.security;

import com.google.gson.Gson;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.redis.UserKey;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 登录成功控制器
 * @author: tssaber
 * @time: 2019/11/26 0026 23:36
 */
@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(AjaxAuthenticationSuccessHandler.class);

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    @Resource
    private RedisUtils redisUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("进入登录成功控制器");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setContentType("text/xml;charset=UTF-8");
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "token");
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String token = redisUtils.get(UserKey.token,jwtUser.getUsername(),String.class);
        httpServletResponse.getWriter().print(GSON.toJson(ApiResponse.success(token)));
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }
}

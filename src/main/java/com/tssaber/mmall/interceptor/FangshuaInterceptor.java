package com.tssaber.mmall.interceptor;

import com.google.gson.Gson;
import com.tssaber.mmall.annotation.AccessLimit;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.redis.UserKey;
import com.tssaber.mmall.service.Impl.UserServiceImpl;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @Author:tssaber 防止相同ip多次访问接口  保护接口
 * @Date: 2020/2/1 17:53
 * @Version 1.0
 */
@Component
public class FangshuaInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private RedisUtils redisUtils;

    private static final Logger log = LoggerFactory.getLogger(FangshuaInterceptor.class);

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入防刷api");
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            if (accessLimit == null){
                return true;
            }
            int maxCount = accessLimit.maxCount();
            UserDto userDto = userService.selUser(request);
            String username = userDto.getUsername();
            Integer count = redisUtils.get(UserKey.apiPrefix,username,Integer.class);
            if (count == null){
                //第一次访问 创建一个redis String 的实例 之后可以使用hash等
                redisUtils.set(UserKey.apiPrefix,username,1);
            }else if (count < maxCount){
                //不是第一次 就自增
                redisUtils.incr(UserKey.apiPrefix,username);
            }else {
                render(response,CExceptionEnums.API_OVER_USE);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CExceptionEnums cExceptionEnums) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(GSON.toJson(ApiResponse.error(cExceptionEnums)));
        out.flush();
        out.close();
    }
}

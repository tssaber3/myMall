package com.tssaber.mmall.interceptor;

import com.tssaber.mmall.annotation.PassToken;
import com.tssaber.mmall.annotation.UserLoginToken;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.redis.UserKey;
import com.tssaber.mmall.security.UserDetailServiceImpl;
import com.tssaber.mmall.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/26 0026 23:10
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Resource
    private UserDetailServiceImpl userDetailService;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/xml;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Headers", "token");

        String token = request.getHeader("token");

        log.info("进入拦截器");
        if (!(handler instanceof HandlerMethod)){
            log.info("不是请求控制器资源放行");
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(PassToken.class)){
            log.info("有pass注解 直接放行");
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()){
                return true;
            }
        }

        if (method.isAnnotationPresent(UserLoginToken.class)){
            log.info("有UserLoginToken注解 需要验证");
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()){
                if (token == null){
                    log.info("token为空,重新登录");
                    throw new GlobalException(CExceptionEnums.TOKEN_ERROR);
                }

                try {
                    JwtTokenUtil.verifyToken(token);
                }catch (Exception e){
                    log.error("error token {}",e.getMessage());
                    throw new GlobalException(CExceptionEnums.TOKEN_ERROR);
                }

                String username;
                try {
                    username = JwtTokenUtil.getUsername(token);
                }catch (Exception e){
                    log.error("error token {}",e.getMessage());
                    throw new RuntimeException("401 没有权限");
                }
                log.info("进入spring security验证");
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                if (userDetails == null){
                    throw new GlobalException(CExceptionEnums.USER_NOT_EXIST);
                }
                log.info("进入redis获取token");
                String redisToken = redisUtils.get(UserKey.token,username,String.class);
                if (!redisToken.equals(token)){
                    throw new GlobalException(CExceptionEnums.TOKEN_ERROR);
                }

                /**
                 * 如果userDetails 和 redis 都成功拿到对象 但是SecurityContextHolder所持有的SecurityContext中去没有对象
                 * 则创建一个 Authentication具体实现类 如UsernamePasswordAuthenticationToken
                 * 供后续的程序进行调用，如访问权限的鉴定等。
                 */
                if (SecurityContextHolder.getContext().getAuthentication() == null){
                    log.info("SecurityContext中 没有Authentication具体实现类 所以创建中");
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                return true;
            }
        }
        log.info("离开拦截器");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

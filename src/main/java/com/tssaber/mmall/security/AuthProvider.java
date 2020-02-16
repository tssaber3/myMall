package com.tssaber.mmall.security;

import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.redis.UserKey;
import com.tssaber.mmall.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: security的验证模块
 * @author: tssaber
 * @time: 2019/11/26 0026 22:09
 */
@Component
public class AuthProvider implements AuthenticationProvider {

    @Resource
    private UserDetailServiceImpl userDetailService;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private RedisUtils redisUtils;

    private static final Logger log = LoggerFactory.getLogger(AuthProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("进入authenticate验证模块");
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        JwtUser jwtUser = (JwtUser) userDetailService.loadUserByUsername(username);
        if (jwtUser == null){
            log.info("用户没找到");
//            throw new GlobalException(CExceptionEnums.USER_NOT_EXIST);
            return null;
        }

        if (bCryptPasswordEncoder.matches(password,jwtUser.getPassword())){
            String token = JwtTokenUtil.createToken(jwtUser);
            log.info("密码匹配成功 生成的token是:{}",token);
            redisUtils.set(UserKey.token,jwtUser.getUsername(),token);
            return new UsernamePasswordAuthenticationToken(jwtUser,null,jwtUser.getAuthorities());
        }else {
//            throw new GlobalException(CExceptionEnums.PASSWORD_ERROR);
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}

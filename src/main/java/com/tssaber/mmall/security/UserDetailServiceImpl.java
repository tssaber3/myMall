package com.tssaber.mmall.security;

import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.entity.pojo.User;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.service.Impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: security的相关类 用来完善用户权限信息的
 * @author: tssaber
 * @time: 2019/11/26 0026 21:30
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserServiceImpl userService;

    private static final Logger log = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("进入loadUserByUsername方法");
        if (s == null || s.length() <= 0){
            log.info("用户名为空");
//            throw new GlobalException(CExceptionEnums.USERNAME_EMPTY);
            return null;
        }
        User user = userService.selUserByUsername(s);
        if (user != null){
            return new JwtUser(user);
        }else {
            log.info("没找到用户");
//            throw new GlobalException(CExceptionEnums.USER_NOT_EXIST);
            return null;
        }
    }
}

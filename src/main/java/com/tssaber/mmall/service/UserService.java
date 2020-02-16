package com.tssaber.mmall.service;

import com.tssaber.mmall.entity.pojo.User;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/26 0026 21:30
 */
@Service
public interface UserService {

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    User selUserByUsername(String username);

    /**
     * 得到用户信息
     * @param request
     * @return
     */
    UserDto selUser(HttpServletRequest request);

    /**
     * 改变密码
     * @param password:新旧密码校验
     * @param request:用来鉴别用户是哪个
     * @return
     */
    boolean changePassword(String password,HttpServletRequest request);

    /**
     * 注册用户
     * @param userInfo:(JSON)手机号和密码
     * @return
     */
    String addUser(String userInfo);

    /**
     * 更新用户信息
     * @param userInfo
     * @param request
     */
    void updUserInfo(String userInfo,HttpServletRequest request);
}

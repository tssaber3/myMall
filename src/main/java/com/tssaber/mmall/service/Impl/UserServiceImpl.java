package com.tssaber.mmall.service.Impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.dao.UserMapper;
import com.tssaber.mmall.entity.pojo.User;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.redis.UserKey;
import com.tssaber.mmall.security.JwtUser;
import com.tssaber.mmall.security.UserDetailServiceImpl;
import com.tssaber.mmall.service.UserService;
import com.tssaber.mmall.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/26 0026 21:31
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private UserDetailServiceImpl userDetailService;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User selUserByUsername(String username) {
        return userMapper.selUserByUsername(username);
    }

    @Override
    public UserDto selUser(HttpServletRequest request) {
        log.info("进入selUser方法");
        String username = "";
        try {
            String token = request.getHeader("token");
            username = JwtTokenUtil.getUsername(token);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(CExceptionEnums.TOKEN_ERROR);
        }
        UserDto userDto = redisUtils.hget(UserKey.userInfo,"userInfo",UserDto.class,username).get(0);
        if (userDto != null){
            log.info("从缓存中拿取对象");
            return userDto;
        }
        log.info("缓存中不存在,从数据库中去拿取");
        User user = userMapper.selUserByUsername(username);
        System.out.println(user);
        if (user != null){
            UserDto dto = new UserDto(user);
            redisUtils.hset(UserKey.userInfo,"userInfo",username,dto);
            return dto;
        }else {
            throw new GlobalException(CExceptionEnums.USER_NOT_EXIST);
        }
    }

    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public boolean changePassword(String password, HttpServletRequest request) {
        String newPassword = null;
        String oldPassword = null;
        try {
            JsonObject jsonObject = new JsonParser().parse(password).getAsJsonObject();
             newPassword = jsonObject.get("newPassword").getAsString();
             oldPassword = jsonObject.get("oldPassword").getAsString();
        }catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        String username = null;
        try {
            String token = request.getHeader("token");
            username = JwtTokenUtil.getUsername(token);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(CExceptionEnums.TOKEN_ERROR);
        }
        User user = userMapper.selUserByUsername(username);
        if(bCryptPasswordEncoder.matches(oldPassword,user.getPassword())){
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            user.setModifiedTime(new Timestamp(System.currentTimeMillis()));
            boolean bok = userMapper.updUserPassword(user);
            redisUtils.del(UserKey.token,username);
            redisUtils.hdel(UserKey.userInfo,"userInfo",username);
            return bok;
        }else {
            throw new GlobalException(CExceptionEnums.PASSWORD_ERROR);
        }
    }

    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public String addUser(String userInfo) {
        User user = null;
        try {
            JsonObject object = new JsonParser().parse(userInfo).getAsJsonObject();
            user = new User(object.get("username").getAsString(),
                    bCryptPasswordEncoder.encode(object.get("password").getAsString()));
            user.setCreateTime(new Timestamp(System.currentTimeMillis()));
            user.setModifiedTime(new Timestamp(System.currentTimeMillis()));
            user.setMoney(new BigDecimal(0));
        }catch (Exception e){
            log.error(e.getMessage());
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        userMapper.insUser(user);
        boolean bok = userMapper.insAuthority(user);
        if (bok){
            JwtUser jwtUser = (JwtUser) userDetailService.loadUserByUsername(user.getUsername());
            if (jwtUser == null){
                throw new GlobalException(CExceptionEnums.USER_NOT_EXIST);
            }
            String token = JwtTokenUtil.createToken(jwtUser);
            log.info("密码匹配成功，生成token:{}",token);
            redisUtils.set(UserKey.token,jwtUser.getUsername(),token);
            return token;
        }else {
            throw new GlobalException(CExceptionEnums.SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updUserInfo(String userInfo, HttpServletRequest request) {
        UserDto userDto = selUser(request);
        try {
            JsonObject object = new JsonParser().parse(userInfo).getAsJsonObject();
            userDto.setNickName(object.get("nickName").getAsString());
            userDto.setAddress(object.get("address").getAsString());
        }catch (Exception e){
            log.info("json解析错误");
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        userDto.setModifiedTime(new Timestamp(System.currentTimeMillis()));
        //这里会删除缓存中的用户信息
        userMapper.updUser(userDto);
        redisUtils.hdel(UserKey.userInfo,"userInfo",userDto.getUsername());
    }
}

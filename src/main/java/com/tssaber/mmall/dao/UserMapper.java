package com.tssaber.mmall.dao;

import com.tssaber.mmall.entity.pojo.User;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/26 0026 15:55
 */
@Repository
public interface UserMapper {
    /**
     * 插入用户
     * @param user
     * @return
     */
    boolean insUser(User user);

    /**
     * 插入用户与角色表的映射
     * @param user
     * @return
     */
    boolean insAuthority(User user);

    /**
     * 通过id获取对应用户
     * @param id
     * @return
     */
    User selUserById(@Param("id") int id);

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    User selUserByUsername(@Param("username") String username);

    /**
     * 更新用户密码
     * @param user
     * @return
     */
    boolean updUserPassword(User user);

    /**
     * 更新用户
     * @param userDto
     * @return
     */
    boolean updUser(UserDto userDto);
}

package com.tssaber.mmall.security;

import com.tssaber.mmall.entity.pojo.Authority;
import com.tssaber.mmall.entity.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @description: 包含了用户的信息和相关的权限信息
 * @author: tssaber
 * @time: 2019/11/26 0026 21:20
 */
public class JwtUser implements UserDetails {

    private String username;

    private String password;

    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    public JwtUser(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        List<Authority> list = user.getRoles();
        for (Authority authority:list){
            authorities.add(new SimpleGrantedAuthority(authority.getRole().getName()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

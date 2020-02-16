package com.tssaber.mmall.entity.pojo.dto;

import com.tssaber.mmall.entity.pojo.User;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author:tssaber
 * @Date: 2020/1/26 13:51
 * @Version 1.0
 */
public class UserDto {

    private Integer id;
    private String username;
    private String nickName;
    private String email;
    private String phone;
    private BigDecimal money;
    private String address;
    private Timestamp modifiedTime;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", money=" + money +
                ", address='" + address + '\'' +
                ", modifiedTime=" + modifiedTime +
                '}';
    }

    public UserDto(){

    }

    public UserDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.money = user.getMoney();
        this.address = user.getDeliveryAddress();
        this.nickName = user.getNickName();
    }

    public Timestamp getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Timestamp modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}

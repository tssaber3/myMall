package com.tssaber.mmall.entity.pojo;

import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/24 0024 22:31
 */
@Alias("mmall_user")
@Entity
@Table(name = "mmall_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "phone")
    private String phone;

    @Column(name = "question")
    private String question;

    @Column(name = "nickname")
    private String nickName;

    @Column(name = "answer")
    private String answer;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "gmt_create")
    private Timestamp createTime;

    @Column(name = "gmt_modified")
    private Timestamp modifiedTime;

    private transient List<Authority> roles;

    public User(){}

    public User(String username,String password){
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", money=" + money +
                ", phone='" + phone + '\'' +
                ", question='" + question + '\'' +
                ", nickName='" + nickName + '\'' +
                ", answer='" + answer + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", createTime=" + createTime +
                ", modifiedTime=" + modifiedTime +
                ", roles=" + roles +
                '}';
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<Authority> getRoles() {
        return roles;
    }

    public void setRoles(List<Authority> roles) {
        this.roles = roles;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Timestamp modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}

package com.tssaber.mmall.entity.pojo;

import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/26 0026 16:50
 */
@Alias("mmall_authority")
@Entity
@Table(name = "mmall_authority")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "gmt_create")
    private Timestamp createTime;

    @Column(name = "gmt_modified")
    private Timestamp modifiedTime;

    private transient Role role;

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleId=" + roleId +
                ", createTime=" + createTime +
                ", modifiedTime=" + modifiedTime +
                ", role=" + role +
                '}';
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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
}

package com.javacode.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户信息表
 */
@Table(name = "user")
public class User {
    /*
     * 主键
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //自增
    private Integer id;
    private String name;

    private String phone;
    private String type;
    private String password;

    //Transient声明不是属于数据库的方法
    @Transient
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
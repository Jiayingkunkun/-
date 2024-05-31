package com.javacode.entity;

import javax.persistence.*;

/**
 * 用户信息表
 */
@Table(name = "history")
public class History {
    /*
     * 主键
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //自增
    private Integer id;
    private Long userid;

    private String newsids;

    public String getNewsids() {
        return newsids;
    }

    public void setNewsids(String newsids) {
        this.newsids = newsids;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
package com.javacode.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 *公告
 */
@Table(name = "notice")
public class Notice {
    /*
     * 主键
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //自增
    private Long id;

    private String time;

    private String title;

    public Long getClick() {
        return click;
    }

    public void setClick(Long click) {
        this.click = click;
    }

    private Long click;

    private String content;

    /**
     * 图片
     */
    private String picture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Transient
    private List<Long> fileList;

    public void setFileList(List<Long> fileList) {
        this.fileList = fileList;
    }

    public List<Long> getFileList() {
        return fileList;
    }
}
package com.javacode.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *文件
 */
@Table(name = "fileinfo")
public class Fileinfo {
    /*
     * 主键
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //自增
    private Long id;

    private String originname;

    private String filename;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginname() {
        return originname;
    }

    public void setOriginname(String originname) {
        this.originname = originname;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
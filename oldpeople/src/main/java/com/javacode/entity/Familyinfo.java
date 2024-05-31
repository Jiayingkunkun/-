package com.javacode.entity;

import com.javacode.service.OldinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * 家属信息表
 */
@Service
@Table(name = "familyinfo")
public class Familyinfo {
    /*
     * 主键
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //自增
    private Integer fno;

    private String name;

    private String fsex;

    private Integer fage;
    private Date birthdate;

    private String phone;

    private String oldlist;

    private String relation;

    /*
    老人姓名,数据表中是id，这里转换为姓名
     */
    @Transient
    private String oldname;

    public Integer getFno() {
        return fno;
    }

    public void setFno(Integer fno) {
        this.fno = fno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFsex() {
        return fsex;
    }

    public void setFsex(String fsex) {
        this.fsex = fsex;
    }

    public Integer getFage() {
        return fage;
    }

    public void setFage(Integer fage) {
        this.fage = fage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOldlist() {
        return oldlist;
    }

    public void setOldlist(String oldlist) {
        this.oldlist = oldlist;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getOldname() {
        return oldname;
    }

    public void setOldname(String oldname) {
        this.oldname = oldname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }


    //获取家属表中老人id列表中的id
    // 修改 getOldInfoList() 方法，只返回老人信息的 ID 列表
    public List<Long> getOldInfoIds() {
        List<Long> oldInfoIds = new ArrayList<>();
        if (oldlist != null && !oldlist.isEmpty()) {
            // 去掉字符串中的方括号和空格，然后按逗号分隔得到ID数组
            String[] idArray = oldlist.replaceAll("\\[|\\]|\\s", "").split(",");
            // 遍历ID数组，将字符串ID转换为Long类型并添加到列表中
            for (String id : idArray) {
                oldInfoIds.add(Long.parseLong(id.trim()));
            }
        }
        return oldInfoIds;
    }
}
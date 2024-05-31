package com.javacode.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javacode.common.ResultCode;
import com.javacode.entity.Familyinfo;
import com.javacode.exception.CustomException;
import com.javacode.mapper.FamilyinfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 家属相关的service
 */
@Service
public class FamilyinfoService {

    @Resource
    private FamilyinfoMapper familyinfoMapper;

    /**
     * 分页查询家属列表
     */
    public PageInfo<Familyinfo> findPage(Integer pageNum, Integer pageSize, String name){
        PageHelper.startPage(pageNum, pageSize);
        List<Familyinfo> list = familyinfoMapper.findByName(name);
        return PageInfo.of(list);
    }

    /**
     * 新增家属
     * （写完service到controller层调用）
     */
    public Familyinfo add(Familyinfo familyinfo){
        //判断家属是否已存在，先写familyinfomapper.java，再自动生成familyinfomapper.xml后在xml里写
        //根据电话判断
        int count = familyinfoMapper.checkRepeat("phone",familyinfo.getPhone());
        if(count>0){
            throw new CustomException(ResultCode.FAMILY_EXIST_ERROR);
        }
        //insertSelective()返回int，1为新增成功
        familyinfoMapper.insertSelective(familyinfo);
        return familyinfo; //返回给前端
    }

    /**
     * 修改家属
     */
    public void update(Familyinfo familyinfo){
        familyinfoMapper.updateByPrimaryKeySelective(familyinfo);
    }

    /**
     * 根据id删除家属
     */
    public void delete(Integer id){
        familyinfoMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据用户id查询家属表对应记录
     */
    public Familyinfo getFamilyInfoByUserId(Long userId) {
        return familyinfoMapper.findByUserId(userId);
    }

}
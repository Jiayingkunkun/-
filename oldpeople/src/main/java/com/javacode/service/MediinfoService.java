package com.javacode.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javacode.common.ResultCode;
import com.javacode.entity.Mediinfo;
import com.javacode.entity.Mediinfo;
import com.javacode.entity.Mediinfo;
import com.javacode.exception.CustomException;
import com.javacode.mapper.MediinfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 药品相关的service
 */
@Service
public class MediinfoService {

    @Resource
    private MediinfoMapper mediinfoMapper;

    /**
     * 获取所有药品信息
     */
    public List<Mediinfo> getAllMediinfo() {
        return mediinfoMapper.findAllMediinfo(); // Assuming you have a method in your mapper to retrieve all Mediinfo objects
    }

    /**
     * 分页查询药品列表
     */
    public PageInfo<Mediinfo> findPage(Integer pageNum, Integer pageSize, String name){
        PageHelper.startPage(pageNum, pageSize);
        List<Mediinfo> list = mediinfoMapper.findByName(name);
        return PageInfo.of(list);
    }

    /**
     * 新增药品
     */
    public Mediinfo add(Mediinfo mediinfo){
        mediinfoMapper.insertSelective(mediinfo);

        return mediinfo;
    }


    /**
     * 修改药品
     */
    public void update(Mediinfo mediinfo){
        mediinfoMapper.updateByPrimaryKeySelective(mediinfo);
    }


    /**
     * 根据id删除药品
     */
    public void delete(Integer id){
        mediinfoMapper.deleteByPrimaryKey(id);
    }

    /**
     *根据id查询一条药品
     */
    public Mediinfo findById(Long id){
        return mediinfoMapper.selectByPrimaryKey(id);
    }

}
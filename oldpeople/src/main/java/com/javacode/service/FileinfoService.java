package com.javacode.service;

import com.javacode.entity.Fileinfo;
import com.javacode.mapper.FileinfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 文件相关的service
 */
@Service
public class FileinfoService {

    @Resource
    private FileinfoMapper fileinfoMapper;

    /**
     * 新增文件
     * （写完service到controller层调用）
     */
    public Fileinfo add(Fileinfo fileinfo){
        fileinfoMapper.insertSelective(fileinfo);
        return fileinfo; //返回给前端
    }

    /**
     * 修改文件
     */
    public void update(Fileinfo fileinfo){
        fileinfoMapper.updateByPrimaryKeySelective(fileinfo);
    }

    /**
     * 根据id删除文件
     */
    public void delete(Long id){
        fileinfoMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据id获取
     */
    public Fileinfo findById(Long id){
        return fileinfoMapper.selectByPrimaryKey(id);
    }
}
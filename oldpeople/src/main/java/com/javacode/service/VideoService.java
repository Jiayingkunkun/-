package com.javacode.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javacode.common.ResultCode;
import com.javacode.entity.Video;
import com.javacode.entity.Video;
import com.javacode.exception.CustomException;
import com.javacode.mapper.VideoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 监控相关的service
 */
@Service
public class VideoService {

    @Resource
    private VideoMapper videoMapper;

    /**
     * 分页查询监控列表
     */
    public PageInfo<Video> findPage(Integer pageNum, Integer pageSize, String name){
        PageHelper.startPage(pageNum, pageSize);
        List<Video> list = videoMapper.findByName(name);
        return PageInfo.of(list);
    }

    /**
     * 新增监控
     */
    public Video add(Video video){
        videoMapper.insertSelective(video);
        return video;
    }


    /**
     * 修改监控
     */
    public void update(Video video){
        videoMapper.updateByPrimaryKeySelective(video);
    }

    /**
     * 根据id删除监控
     */
    public void delete(Integer id){
        videoMapper.deleteByPrimaryKey(id);
    }

    /**
     *根据id查询一条监控
     */
    public Video findById(Integer id){
        return videoMapper.selectByPrimaryKey(id);
    }

    /*监控总数*/
    public Integer count(){
        return videoMapper.count();
    }

    /*网格员总数*/
    public Integer count1(){
        return videoMapper.count1();
    }
}
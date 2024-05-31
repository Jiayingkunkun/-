package com.javacode.service;


import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javacode.entity.Notice;
import com.javacode.entity.Oldinfo;
import com.javacode.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 公告相关的service
 */
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 分页查询公告列表
     */
    public PageInfo<Notice> findPage(Integer pageNum, Integer pageSize, String title){
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.findByTitle(title);
        return PageInfo.of(list);
    }

    /**
     * 分页查询所有公告列表
     */
    public PageInfo<Notice> findPages(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.getAllNotices(); // 获取所有公告
        return PageInfo.of(list);
    }

    /**
     * 页面传来的上传文件列表转换成以逗号隔开的id列表
     */
    private void convertFileListToField(Notice notice){
        List<Long> fileList = notice.getFileList();
        if(!CollectionUtil.isEmpty(fileList)){
            notice.setPicture(fileList.toString());
        }
    }
    /**
     * 新增公告
     */
    public Notice add(Notice notice){
        convertFileListToField(notice);
        notice.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        noticeMapper.insertSelective(notice);
        return notice;
    }
    /**
     * 修改公告
     */
    public void update(Notice notice){
        convertFileListToField(notice);
        notice.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        noticeMapper.updateByPrimaryKeySelective(notice);
    }

    /**
     * 根据id删除公告
     */
    public void delete(Long id){
        noticeMapper.deleteByPrimaryKey(id);
    }

    /**
     *根据id查询一条公告
     */
    public Notice findById(Long id){
        return noticeMapper.selectByPrimaryKey(id);
    }

    /**
     * 增加点击次数
     */
    public void incrementClickCount(Long id) {
        Notice notice = noticeMapper.findById(id);
        if (notice != null) {
            Long clickCount = notice.getClick();
            if (clickCount == null) {
                clickCount = 0L; // 如果click字段为空，初始化为0
            }
            clickCount++; // 增加点击次数
            notice.setClick(clickCount);
            noticeMapper.updateClickCount(id);
        }
    }

    //根据疾病信息检索公告
    public List<Notice> getNoticesByDisease(String diseaseName) {
        return noticeMapper.findByDiseaseName(diseaseName);
    }

    //获取所有公告
    public List<Notice> getAllNotices() {
        return noticeMapper.getAllNotices();
    }

}

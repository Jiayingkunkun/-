package com.javacode.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javacode.entity.Feedback;
import com.javacode.mapper.FeedbackMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 反馈相关的service
 */
@Service
public class FeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;

    /**
     * 分页查询反馈列表
     */
    public PageInfo<Feedback> findPage(Integer pageNum, Integer pageSize, String title){
        PageHelper.startPage(pageNum, pageSize);
        List<Feedback> list = feedbackMapper.findByTitle(title);
        return PageInfo.of(list);
    }

    /**
     * 新增反馈
     */
    public Feedback add(Feedback feedback){
        feedback.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        feedbackMapper.insertSelective(feedback);
        return feedback;
    }

    /**
     * 修改反馈
     */
    public void update(Feedback feedback){
        feedback.setStatus(1);
        //设置回复时间replyTime
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String formattedDate = sdf.format(currentDate); // 格式化当前日期为字符串
        try {
            Date replyTime = sdf.parse(formattedDate); // 将字符串解析为日期对象
            feedback.setReplytime(replyTime); // 设置了回复时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        feedbackMapper.updateByPrimaryKeySelective(feedback);
    }

    /**
     * 根据id删除反馈
     */
    public void delete(Long id){
        feedbackMapper.deleteByPrimaryKey(id);
    }

    /**
     *根据id查询一条反馈
     */
    public Feedback findById(Long id){
        return feedbackMapper.selectByPrimaryKey(id);
    }

    /*待处理总数*/
    public Integer count(){
        return feedbackMapper.count();
    }
}

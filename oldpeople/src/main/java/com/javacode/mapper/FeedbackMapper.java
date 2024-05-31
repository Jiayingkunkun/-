package com.javacode.mapper;

import com.javacode.entity.Feedback;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface FeedbackMapper extends Mapper<Feedback> {
    /*根据公告标题模糊查询*/
    List<Feedback> findByTitle(@Param("title") String title);
    /*反馈总数*/
    @Select("select count(*) from feedback where status=0 ")
    Integer count();
}
package com.javacode.mapper;

import com.javacode.entity.Video;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface VideoMapper extends Mapper<Video> {
    /*根据监控名查询*/
    List<Video> findByName(@Param("name") String name);

    /*监控唯一性判断*/
    int checkRepeat(@Param("column")String column, @Param("value")String value);

    /*监控总数*/
    @Select("select count(*) from video")
    Integer count();

    /*网格员总数*/
    @Select("select count(*) from video where type='网格员'")
    Integer count1();
}
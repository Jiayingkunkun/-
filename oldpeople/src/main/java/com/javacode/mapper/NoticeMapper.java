package com.javacode.mapper;

import com.javacode.entity.Notice;
import com.javacode.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
/**
 *公告相关的Mapper
 */
@Repository
public interface NoticeMapper extends Mapper<Notice> {
    /*根据公告标题模糊查询*/
    List<Notice> findByTitle(@Param("title") String title);

    @Select("SELECT * FROM notice WHERE id = #{id}")
    Notice findById(@Param("id") Long id);

    //获取所有公告
    @Select("SELECT * FROM notice")
    List<Notice> getAllNotices();

    // 根据疾病名称检索公告
    List<Notice> findByDiseaseName(@Param("diseaseName") String diseaseName);

    @Update("UPDATE notice SET click = COALESCE(click, 0) + 1 WHERE id = #{id}")
    void updateClickCount(@Param("id") Long id);

    /*用户唯一性判断*/
   // int checkRepeat(@Param("column")String column, @Param("value")String value);
}
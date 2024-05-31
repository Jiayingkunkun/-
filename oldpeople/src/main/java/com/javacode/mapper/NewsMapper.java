package com.javacode.mapper;

import com.javacode.entity.News;
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
public interface NewsMapper extends Mapper<News> {
	/*根据公告标题模糊查询*/
	List<News> findByTitle(@Param("title") String title);

	@Select("SELECT * FROM news WHERE id = #{id}")
	News findById(@Param("id") Long id);

	//获取所有公告
	@Select("SELECT * FROM news")
	List<News> getAllNewss();

	// 根据疾病名称检索公告
	List<News> findByDiseaseName(@Param("diseaseName") String diseaseName);

	@Update("UPDATE news SET click = COALESCE(click, 0) + 1 WHERE id = #{id}")
	void updateClickCount(@Param("id") Long id);

	@Update("UPDATE news SET likes = COALESCE(likes, 0) + 1 WHERE id = #{id}")
	void updateLikeCount(@Param("id") Long id);
	List<String> getAllNewsTypes();

	// 根据新闻标题模糊查询
	List<News> findByTitleContaining(String title);

	/*用户唯一性判断*/
	// int checkRepeat(@Param("column")String column, @Param("value")String value);
}
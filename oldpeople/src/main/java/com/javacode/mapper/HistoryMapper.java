package com.javacode.mapper;

import com.javacode.entity.History;
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
public interface HistoryMapper extends Mapper<History> {



	//获取所有公告
	@Select("SELECT * FROM history")
	List<History> getAllHistorys();

	List<History> findByUserId(Long userId);


	@Update("UPDATE history SET click = COALESCE(click, 0) + 1 WHERE id = #{id}")
	void updateClickCount(@Param("id") Long id);


	/*用户唯一性判断*/
	// int checkRepeat(@Param("column")String column, @Param("value")String value);
}
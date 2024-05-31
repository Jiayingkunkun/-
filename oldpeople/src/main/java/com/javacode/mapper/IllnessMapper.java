package com.javacode.mapper;

import com.javacode.entity.Illness;
import com.javacode.entity.Oldinfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 慢性病相关Mapper
 */
@Repository
public interface IllnessMapper extends Mapper<Illness> {

    /*根据姓名查询*/
    List<Illness> findByName(@Param("name") String name, @Param("id") Long id);

    /*慢性病唯一性判断*/
    int checkRepeat(@Param("column")String column, @Param("value")String value);
}
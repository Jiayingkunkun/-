package com.javacode.mapper;

import com.javacode.entity.Familyinfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 家属相关Mapper
 */
@Repository
public interface FamilyinfoMapper extends Mapper<Familyinfo> {

    /*根据姓名查询*/
    List<Familyinfo> findByName(@Param("name") String name);

    /*家属唯一性判断*/
    int checkRepeat(@Param("column")String column, @Param("value")String value);


    // 根据用户ID查询家庭信息
    Familyinfo findByUserId(Long userId);

}
package com.javacode.mapper;

import com.javacode.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserMapper extends Mapper<User> {
    /*根据用户名查询*/
    List<User> findByName(@Param("name") String name);

    /*用户唯一性判断*/
    int checkRepeat(@Param("column")String column, @Param("value")String value);

    /*用户总数*/
    @Select("select count(*) from user")
    Integer count();

    /*网格员总数*/
    @Select("select count(*) from user where type='网格员'")
    Integer count1();
}
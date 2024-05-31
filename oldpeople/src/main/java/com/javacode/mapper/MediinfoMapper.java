package com.javacode.mapper;

import com.javacode.entity.Mediinfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface MediinfoMapper extends Mapper<Mediinfo> {
    /*根据药品名查询*/
    List<Mediinfo> findByName(@Param("name") String name);

    /* 查询所有Mediinfo信息 */
    List<Mediinfo> findAllMediinfo();
}

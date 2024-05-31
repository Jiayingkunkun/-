package com.javacode.mapper;

import com.javacode.entity.Fileinfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 文件相关Mapper
 */
@Repository
public interface FileinfoMapper extends Mapper<Fileinfo> {

}
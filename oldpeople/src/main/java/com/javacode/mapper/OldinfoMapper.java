package com.javacode.mapper;

import com.javacode.entity.Oldinfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface OldinfoMapper extends Mapper<Oldinfo> {

    /*根据姓名查询，或者根据id查询*/
    List<Oldinfo> findByName(@Param("name") String name, @Param("id") Long id);

    /*老人唯一性判断*/
    int checkRepeat(@Param("column")String column, @Param("value")String value);

    /*老人总数*/
    @Select("select count(*) from oldinfo")
    Integer count();

    /*老人年龄区间数目*/
    @Select("SELECT \n" +
            "    'Age 60-70' AS type,\n" +
            "    SUM(CASE WHEN eage >= 60 AND eage <= 70 THEN 1 ELSE 0 END) AS count\n" +
            "FROM \n" +
            "    oldinfo\n" +
            "UNION ALL\n" +
            "SELECT \n" +
            "    'Age 70-85' AS type,\n" +
            "    SUM(CASE WHEN eage > 70 AND eage <= 85 THEN 1 ELSE 0 END) AS count\n" +
            "FROM \n" +
            "    oldinfo\n" +
            "UNION ALL\n" +
            "SELECT \n" +
            "    'Age 85-90' AS type,\n" +
            "    SUM(CASE WHEN eage > 85 AND eage <= 90 THEN 1 ELSE 0 END) AS count\n" +
            "FROM \n" +
            "    oldinfo\n" +
            "UNION ALL\n" +
            "SELECT \n" +
            "    'Age above 90' AS type,\n" +
            "    SUM(CASE WHEN eage > 90 THEN 1 ELSE 0 END) AS count\n" +
            "FROM \n" +
            "    oldinfo;\n")
    List<Map<String,Object>> getEagenumber();

    // 根据传入的老人信息 ID 列表查询老人信息列表
    @Select("<script>" +
            "SELECT * FROM oldinfo WHERE id IN " +
            "<foreach collection='oldInfoIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Oldinfo> findOldinfosByIds(@Param("oldInfoIds") List<Long> oldInfoIds);
    List<Oldinfo> getOldinfoByUserId(Long userId);
    // 根据老人的 ID 查询老人信息
    Oldinfo getOldinfoById(@Param("oldInfoId") Long oldInfoId);
}
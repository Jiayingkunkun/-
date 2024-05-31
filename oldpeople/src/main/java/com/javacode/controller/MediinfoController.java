package com.javacode.controller;

import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.Mediinfo;
import com.javacode.service.MediinfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 药品增删改查控制器
 */
@RestController
@RequestMapping(value = "/mediinfo")
public class MediinfoController {

    @Resource
    private MediinfoService mediinfoService;

    /**
     * 分页查询药品列表
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @param name 药品名字
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<Mediinfo>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @PathVariable String name){
        return Result.success(mediinfoService.findPage(pageNum,pageSize,name));
    }

    /**
     * 新增药品
     */
    //@PutMapping用于修改
    @PostMapping
    public Result<Mediinfo> add(@RequestBody Mediinfo mediinfo){
        // 设置当前系统时间为世界标准时间 (UTC)
        Instant currentInstant = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC);
        String formattedDateTime = formatter.format(currentInstant);
        mediinfo.setDatetime(formattedDateTime);
        mediinfoService.add(mediinfo);  //调用service层的方法
        return Result.success(mediinfo);
    }


    /**
     * 更新药品的余量和天数
     * @return
     */
    @PutMapping("/update")
    public Result updateMediinfo() {
        try {
            List<Mediinfo> mediinfoList = mediinfoService.getAllMediinfo();// 获取所有Mediinfo对象
            // 获取当前日期时间
            LocalDateTime currentDateTime = LocalDateTime.now();
            // 遍历每个Mediinfo对象
            for (Mediinfo mediinfo : mediinfoList) {
                // 检查日期字符串是否包含 'Z' 字符
                String dateString = mediinfo.getDate();
                if (dateString.contains("Z")) {
                    // 修正日期字符串格式（去除 'Z'）
                    dateString = dateString.substring(0, dateString.indexOf("Z"));
                }
                // 解析日期时间字符串为 LocalDateTime
                LocalDateTime mediinfoDateTime = LocalDateTime.parse(dateString);
                // 将 LocalDateTime 转换为 LocalDate，仅保留日期部分
                LocalDate mediinfoDate = mediinfoDateTime.toLocalDate();

                long diffDays = ChronoUnit.DAYS.between(mediinfoDate, currentDateTime.toLocalDate());// 计算天数差
                mediinfo.setDay((int) diffDays);  // 更新day字段

                // 获取数据库中存储的时间字符串
                String dateTimeStringFromDB = mediinfo.getDatetime();
                // 解析时间字符串为ZonedDateTime对象
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeStringFromDB);
                // 计算天数之差
                long day = ChronoUnit.DAYS.between(zonedDateTime.toLocalDate(), currentDateTime.toLocalDate());
                Integer a = mediinfo.getAllowance();
                Integer total = mediinfo.getTotal();
                if(a<total){
                    mediinfo.setAllowance(0);
                }else {
                    int newAllowance = (int) (a - (day*mediinfo.getTotal()));
                    mediinfo.setAllowance(newAllowance);
                }
                // 设置当前系统时间为世界标准时间 (UTC)
                Instant currentInstant = Instant.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        .withZone(ZoneOffset.UTC);
                String formattedDateTime = formatter.format(currentInstant);
                mediinfo.setDatetime(formattedDateTime);

                mediinfoService.update(mediinfo);// 对每个Mediinfo对象执行更新操作
            }
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }



    /**
     * 更新药品
     */
    @PutMapping
    public Result update(@RequestBody Mediinfo mediinfo){
        // 设置当前系统时间为世界标准时间 (UTC)
        Instant currentInstant = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC);
        String formattedDateTime = formatter.format(currentInstant);
        mediinfo.setDatetime(formattedDateTime);
        mediinfoService.update(mediinfo);  //调用service层的方法
        return Result.success();
    }

    /**
     * 删除药品
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        mediinfoService.delete(id);
        return Result.success();
    }

    public void updateMediinfoDay() {

    }

}

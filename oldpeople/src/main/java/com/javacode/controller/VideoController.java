package com.javacode.controller;

import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.Video;
import com.javacode.service.VideoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 监控增删改查控制器
 */
@RestController
@RequestMapping(value = "/video")
public class VideoController {

    @Resource
    private VideoService videoService;

    /**
     * 分页查询监控列表
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @param name 监控名字
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<Video>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @PathVariable String name){
        return Result.success(videoService.findPage(pageNum,pageSize,name));
    }

    /**
     * 新增监控
     */
    //@PutMapping用于修改
    @PostMapping
    public Result<Video> add(@RequestBody Video video){
        videoService.add(video);  //调用service层的方法
        return Result.success(video);
    }

    /**
     * 更新监控
     */
    @PutMapping
    public Result update(@RequestBody Video video){
        videoService.update(video);  //调用service层的方法
        return Result.success();
    }

    /**
     * 删除监控
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        videoService.delete(id);
        return Result.success();
    }

    /**
     * 获取监控信息
     */
    @GetMapping("/{id}")
    public Result<Video> detail(@PathVariable Integer id){
        Video video = videoService.findById(id);
        return Result.success(video);
    }
}

package com.javacode.controller;

import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.Feedback;
import com.javacode.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 反馈增删改查控制器
 */
@RestController
@RequestMapping(value = "/feedback")
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    /**
     * 分页查询反馈列表
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @param title 反馈名字
     */
    @GetMapping("/page/{title}")
    public Result<PageInfo<Feedback>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @PathVariable String title){
        return Result.success(feedbackService.findPage(pageNum,pageSize,title));
    }

    /**
     * 新增反馈
     */
    @PostMapping
    public Result<Feedback> add(@RequestBody Feedback feedback){
        feedbackService.add(feedback);
        return Result.success(feedback);
    }

    /**
     * 更新反馈，只增加reply相关的值
     */
    @PutMapping
    public Result update(@RequestBody Feedback feedback){
        feedbackService.update(feedback);
        return Result.success();
    }

    /**
     * 删除反馈
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        feedbackService.delete(id);
        return Result.success();
    }

    /**
     * 根据id查询一条反馈
     */
    @GetMapping ("/{id}")
    public Result detail(@PathVariable Long id){
        return Result.success(feedbackService.findById(id));
    }
}
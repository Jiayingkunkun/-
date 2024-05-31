package com.javacode.controller;

import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.Illness;
import com.javacode.service.IllnessService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 慢性病增删改查控制器
 */
@RestController
@RequestMapping(value = "/illness")
public class IllnessController {

    @Resource
    private IllnessService illnessService;

    /**
     * 分页查询慢性病列表
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @param name 慢性病名字
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<Illness>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "15") Integer pageSize,
                                          @PathVariable String name){
        return Result.success(illnessService.findPage(pageNum,pageSize,name));
    }

    /**
     * 新增慢性病
     */
    //@PutMapping用于修改
    @PostMapping
    public Result<Illness> add(@RequestBody Illness illness){
        illnessService.add(illness);  //调用service层的方法
        return Result.success(illness);
    }

    /**
     * 更新慢性病
     */
    @PutMapping
    public Result update(@RequestBody Illness illness){
        illnessService.update(illness);  //调用service层的方法
        return Result.success();
    }

    /**
     * 删除慢性病
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        illnessService.delete(id);
        return Result.success();
    }
}

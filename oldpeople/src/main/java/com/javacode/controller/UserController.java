package com.javacode.controller;

import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.User;
import com.javacode.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户增删改查控制器
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 分页查询用户列表
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @param name 用户名字
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<User>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @PathVariable String name){
        return Result.success(userService.findPage(pageNum,pageSize,name));
    }

    /**
     * 新增用户
     */
    //@PutMapping用于修改
    @PostMapping
    public Result<User> add(@RequestBody User user){
        userService.add(user);  //调用service层的方法
        return Result.success(user);
    }

    /**
     * 更新用户
     */
    @PutMapping
    public Result update(@RequestBody User user){
        userService.update(user);  //调用service层的方法
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        userService.delete(id);
        return Result.success();
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public Result<User> detail(@PathVariable Integer id){
        User user = userService.findById(id);
        return Result.success(user);
    }
}

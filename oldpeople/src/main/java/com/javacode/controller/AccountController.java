package com.javacode.controller;

import cn.hutool.crypto.SecureUtil;
import com.javacode.common.Common;
import com.javacode.common.Result;
import com.javacode.common.ResultCode;
import com.javacode.entity.User;
import com.javacode.exception.CustomException;
import com.javacode.service.UserService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.hutool.core.util.StrUtil;

/**
 * 登录、退出相关的控制器
 * 返回的数据类型是json,都是一个个的类与对象
 */
@RestController
public class AccountController {

    @Resource
    private UserService userService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user, HttpServletRequest request){
        if(StrUtil.isBlank(user.getName())||StrUtil.isBlank(user.getPassword())){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        //从数据库查询账号密码是否正确,放到session
        User login = userService.login(user.getName(),user.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute(Common.USER,login);
        session.setMaxInactiveInterval(60*24);  //设置过期时间
        return Result.success(login);
    }
//    /**
//     * 登录
//     */
//    @PostMapping("/login")
//    public Result<User> login(@RequestBody User user, HttpServletRequest request){
//        if(StrUtil.isBlank(user.getName())||StrUtil.isBlank(user.getPassword())){
//            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
//        }
//        //从数据库查询账号密码是否正确,放到session
//        User login = userService.login(user.getName(),user.getPassword());
//        HttpSession session = request.getSession();
//        session.setAttribute(Common.USER,login);
//        session.setMaxInactiveInterval(60*24);  //设置过期时间
//
//        // 在这里设置用户类型字段
//        login.setType("家属"); // 这里假设用户类型为家属
//
//        return Result.success(login);
//    }


    /**
     * 重置密码为123456
     */
    @PostMapping("/resetPassword")
    public Result<User> resetPassword(@RequestBody User user){
        return Result.success(userService.resetPassword(user.getName()));
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request){
        request.getSession().setAttribute(Common.USER,null);
        return Result.success();
    }

    /**
     * 判断用户是否已登录
     */
    @GetMapping("/auth")
    public Result getAuth(HttpServletRequest request){
        Object user = request.getSession().getAttribute(Common.USER);
        if(user == null){
            return Result.error("401","未登录");
        }
        return Result.success(user);
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody User user1, HttpServletRequest request){
        //Session数据库里获取的密码
        Object user = request.getSession().getAttribute(Common.USER);
        if(user == null){
            return Result.error("401","未登录");
        }
        User newUser = (User)user;
        //前端传进来的密码
        String oldPassword = SecureUtil.md5(user1.getPassword());
        //如果两个密码不相等,则错误
        if(!oldPassword.equals(newUser.getPassword())){
            return Result.error(ResultCode.USER_ACCOUNT_ERROR.code,ResultCode.USER_ACCOUNT_ERROR.msg);
        }
        newUser.setPassword(SecureUtil.md5(user1.getNewPassword()));
        userService.update(newUser);
        //清空Session,让用户重新登录
        request.getSession().setAttribute(Common.USER,null);
        return Result.success();
    }

}

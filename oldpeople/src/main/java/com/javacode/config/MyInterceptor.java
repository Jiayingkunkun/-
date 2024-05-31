package com.javacode.config;

import com.javacode.common.Common;
import com.javacode.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * 如果没登录,只能访问登录页面
 * 返回true 就继续执行后面的请求，返回false 中断后面的请求，直接转向登录页
 */
public class MyInterceptor implements HandlerInterceptor {

    /**
     * 所有访问后台的请求要先从这里经过
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        User user = (User) request.getSession().getAttribute(Common.USER);
        if(user == null){
            //重定向到登录页
            response.sendRedirect( "/end/page/login.html");
            return false;
        }
        return true;
    }
}

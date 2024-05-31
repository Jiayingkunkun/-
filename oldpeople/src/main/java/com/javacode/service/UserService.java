package com.javacode.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javacode.common.ResultCode;
import com.javacode.entity.User;
import com.javacode.exception.CustomException;
import com.javacode.mapper.UserMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.List;

/**
 * 用户相关的service
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 登录
     */
    public User login(String name,String password){
        //判断数据库里面是否有用户
        List<User> list = userMapper.findByName(name);
        if(CollectionUtil.isEmpty(list)){
            throw new CustomException(ResultCode.USER_NOT_EXIST_ERROR);
        }
        //判断密码是否正确
        if(!SecureUtil.md5(password).equals(list.get(0).getPassword())){
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        return list.get(0);

    }

    /**
     * 重置密码（忘记密码）
     */
    public User resetPassword(String name){
        //判断数据库里是否有该用户
        List<User> list = userMapper.findByName(name);
        if(CollectionUtil.isEmpty(list)){
            throw new CustomException(ResultCode.USER_NOT_EXIST_ERROR);
        }
        list.get(0).setPassword(SecureUtil.md5("123456"));
        userMapper.updateByPrimaryKeySelective(list.get(0));
        return list.get(0);
    }

    /**
     * 分页查询用户列表
     */
    public PageInfo<User> findPage(Integer pageNum, Integer pageSize, String name){
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.findByName(name);
        return PageInfo.of(list);
    }

    /**
     * 新增登录用户
     * （写完service到controller层调用）
     */
    public User add(User user){
        //判断用户是否已存在，先写usermapper.java，再自动生产usermapper.xml后在xml里写
        int count = userMapper.checkRepeat("phone",user.getPhone());
        int name = userMapper.checkRepeat("name",user.getName());
        if(count>0){
            throw new CustomException(ResultCode.USER_EXIST_PHONE_ERROR);
        }
        if(name>0){
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        if(StrUtil.isBlank(user.getPassword())){
            //默认密码123456
            user.setPassword(SecureUtil.md5("123456"));
        }else{
            user.setPassword(SecureUtil.md5(user.getPassword()));
        }
        //insertSelective()返回int，1为新增成功
        userMapper.insertSelective(user);
        return user; //返回给前端
    }

    /**
     * 修改用户
     */
    public void update(User user){
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 根据id删除用户
     */
    public void delete(Integer id){
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     *根据id查询一条用户
     */
    public User findById(Integer id){
        return userMapper.selectByPrimaryKey(id);
    }

    /*用户总数*/
    public Integer count(){
        return userMapper.count();
    }

    /*网格员总数*/
    public Integer count1(){
        return userMapper.count1();
    }

    // 根据用户ID获取用户信息
    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
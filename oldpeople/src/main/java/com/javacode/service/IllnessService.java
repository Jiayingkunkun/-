package com.javacode.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javacode.common.ResultCode;
import com.javacode.entity.Illness;
import com.javacode.entity.Oldinfo;
import com.javacode.exception.CustomException;
import com.javacode.mapper.IllnessMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 慢性病相关的service
 */
@Service
public class IllnessService {

    @Resource
    private IllnessMapper illnessMapper;

    /**
     * 分页查询慢性病列表
     */
    public PageInfo<Illness> findPage(Integer pageNum, Integer pageSize, String name){
        PageHelper.startPage(pageNum, pageSize);
        List<Illness> list = illnessMapper.findByName(name, null);
        return PageInfo.of(list);
    }

    /**
     * 新增慢性病
     * （写完service到controller层调用）
     */
    public Illness add(Illness illness){
        //判断慢性病是否已存在，先写illnessmapper.java，再自动生成illnessmapper.xml后在xml里写
        int count = illnessMapper.checkRepeat("name",illness.getName());
        if(count>0){
            throw new CustomException(ResultCode.FAMILY_EXIST_ERROR);
        }
        //insertSelective()返回int，1为新增成功
        illnessMapper.insertSelective(illness);
        return illness; //返回给前端
    }

    /*根据id查询第一条疾病*/
    public Illness findById(Long id){
        List<Illness> list = illnessMapper.findByName(null, id);
        if(list==null || list.size()==0)
            return null;
        return list.get(0);
    }

    //根据id查询对应疾病信息
    public Illness getIllnessById(Long id) {
        return findById(id);
    }

    /**
     * 修改慢性病
     */
    public void update(Illness illness){
        illnessMapper.updateByPrimaryKeySelective(illness);
    }

    /**
     * 根据id删除慢性病
     */
    public void delete(Integer id){
        illnessMapper.deleteByPrimaryKey(id);
    }
}
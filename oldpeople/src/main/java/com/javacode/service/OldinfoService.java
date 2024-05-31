package com.javacode.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javacode.common.ResultCode;
//import com.javacode.entity.Illness;
import com.javacode.entity.Illness;
import com.javacode.entity.Oldinfo;
import com.javacode.exception.CustomException;
import com.javacode.mapper.OldinfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * 老人相关的service
 */
@Service
public class OldinfoService {

    @Resource
    private OldinfoMapper oldinfoMapper;

    private final IllnessService illnessService;

    public OldinfoService(IllnessService illnessService) {
        this.illnessService = illnessService;
    }

    // 构造函数，用于初始化老人信息列表（假设已经从数据库或其他数据源加载了老人信息）

    /**
     * 分页查询老人列表
     */
    public PageInfo<Oldinfo> findPage(Integer pageNum, Integer pageSize, String name) {
        PageHelper.startPage(pageNum, pageSize);
        List<Oldinfo> list = oldinfoMapper.findByName(name, null);
        return PageInfo.of(list);
    }

    /**
     * 新增老人
     * （写完service到controller层调用）
     */
    public Oldinfo add(Oldinfo oldinfo) {
        convertFileListToField(oldinfo);
        //判断老人是否已存在，先写oldinfomapper.java，再自动生成oldinfomapper.xml后在xml里写
        //根据电话判断
        int count = oldinfoMapper.checkRepeat("phone", oldinfo.getPhone());
        if (count > 0) {
            throw new CustomException(ResultCode.FAMILY_EXIST_ERROR);
        }
        //计算健康指数
        oldinfo.setEhealth(SetEhealth(oldinfo));
        oldinfo.setCreattime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        oldinfoMapper.insertSelective(oldinfo); //insertSelective()返回int，1为新增成功
        return oldinfo; //返回给前端
    }

    /**
     * 根据填写的信息计算老人健康指数
     */
    public Double SetEhealth(Oldinfo oldinfo) {
        Double ehealth = 100.00;
        if ("无".equals(oldinfo.getMate())) { // 使用equals比较字符串
            ehealth *= 0.88;
        }
        if ("有".equals(oldinfo.getBody())) {
            ehealth *= 0.6;
        }
        if ("有".equals(oldinfo.getSmoke())) {
            ehealth *= 0.80;
        }
        if ("有".equals(oldinfo.getDrink())) {
            ehealth *= 0.79;
        }
        if (oldinfo.getIllnessid() != null) {
            List<Integer> illnessIds = parseIllnessIds(oldinfo.getIllnessid());  //将字符串转为列表
            for (Integer illnessId : illnessIds) {
                Illness illness = illnessService.findById(Long.valueOf(illnessId));
                Float number = illness.getNumber();
                ehealth *= number;
            }
        }
        return ehealth;
    }

    // 解析字符串，将其转换为整数列表
    private static List<Integer> parseIllnessIds(String illnessIdsString) {
        List<Integer> illnessIds = new ArrayList<>();
        String[] idsArray = illnessIdsString.replaceAll("\\[|\\]", "").split(",");
        for (String id : idsArray) {
            if (id != null && !id.trim().isEmpty()) {    // 检查id是否为null或空字符串
                try {
                    illnessIds.add(Integer.parseInt(id.trim()));
                } catch (NumberFormatException e) {
                    // 如果无法转换为整数，可以打印错误日志或者进行其他处理
                    e.printStackTrace();
                }
            }
        }
        return illnessIds;
    }

    /**
     * 修改老人
     */
    public void update(Oldinfo oldinfo) {
        convertFileListToField(oldinfo);
        //计算健康指数
        oldinfo.setEhealth(SetEhealth(oldinfo));
        oldinfoMapper.updateByPrimaryKeySelective(oldinfo);
    }

    /**
     * 页面传来的上传文件列表转换成以逗号隔开的id列表
     */
    private void convertFileListToField(Oldinfo oldinfo) {
        List<Long> fileList = oldinfo.getFileList();
        if (!CollectionUtil.isEmpty(fileList)) {
            oldinfo.setPicture(fileList.toString());
        }
    }

    /**
     * 根据id删除老人
     */
    public void delete(Integer id) {
        oldinfoMapper.deleteByPrimaryKey(id);
    }


    /*根据id查询老人     */
    public Oldinfo findById(Long id) {
        List<Oldinfo> list = oldinfoMapper.findByName(null, id);
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    /*老人总数*/
    public Integer count() {
        return oldinfoMapper.count();
    }

    /**
     * 老人年龄区间数目
     */
    public List<Map<String, Object>> getEagenumber() {
        return oldinfoMapper.getEagenumber();
    }

    // 根据传入的老人信息 ID 列表查询老人信息列表
    public List<Oldinfo> findOldinfosByIds(List<Long> oldInfoIds) {
        return oldinfoMapper.findOldinfosByIds(oldInfoIds); // 假设使用了 OldinfoMapper
    }

//    public List<Oldinfo> getOldinfoByUserId(Long userId) {
//        return oldinfoMapper.getOldinfoByUserId(userId);
//    }
//
//    // 根据老人的 ID 获取老人的姓名
//    public String getOldinfoNameById(Long oldInfoId) {
//        Oldinfo oldinfo = oldinfoMapper.getOldinfoById(oldInfoId);
//        return oldinfo != null ? oldinfo.getName() : null;
//    }
//
//    // 根据老人的 ID 获取老人的图片
//    public String getOldinfoImageById(Long oldInfoId) {
//        Oldinfo oldinfo = oldinfoMapper.getOldinfoById(oldInfoId);
//        return oldinfo != null ? oldinfo.getPicture() : null;
//    }


}
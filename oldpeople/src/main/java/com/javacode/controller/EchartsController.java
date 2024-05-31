package com.javacode.controller;

import cn.hutool.json.JSONObject;
import com.javacode.common.Result;
import com.javacode.service.FeedbackService;
import com.javacode.service.OldinfoService;
import com.javacode.service.UserService;
import com.javacode.vo.EchartsData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 后台统计
 */
@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private UserService userService;

    @Resource
    private OldinfoService oldinfoService;

    @Resource
    private FeedbackService feedbackService;

    /**
     * 统计各种总数
     */
    @GetMapping("getTotal")
    public Result<Map<String, Object>> getTotal(){
        Map<String,Object> map = new HashMap<>();
        //获取用户总数
        map.put("totalUser",userService.count());
        //获取评论总数
        map.put("totalOldinfo",oldinfoService.count());
        //获取网格员总数
        map.put("totalGrid",userService.count1());
        //获取待处理总数
        map.put("totalStatus",feedbackService.count());

        return Result.success(map);
    }

    /**
     * 老人年龄区间数目
     */
    @GetMapping("/get/agenumber")
    public Result<List<EchartsData>> getAgeEchartsData(){
        List<EchartsData> list = new ArrayList<>();
        List<Map<String, Object>> eagenumberlist = oldinfoService.getEagenumber();
        Map<String ,Double> typeMap = new HashMap<>();
        for(Map<String,Object>map: eagenumberlist){
            typeMap.put((String) map.get("type"), ((BigDecimal) map.get("count")).doubleValue());
        }
        getPieData("老人年龄分布",list,typeMap);
        getBarData("老人年龄分布",list,typeMap);
        return Result.success(list);
    }

    /**
     * 封装饼图
     * @param name 标题
     * @param pieList 封装完给前端显示的list
     * @param dataMap 传入的数据
     */
    private void getPieData(String name,List pieList,Map<String,Double> dataMap){
        //标题
        EchartsData pieData = new EchartsData();
        Map<String,String> titleMap = new HashMap<>();
        titleMap.put("text", name);
        titleMap.put("left", "center"); // 将标题置于中间
        pieData.setTitle(titleMap);

        // 鼠标移动到图形显示信息
        Map<String,Object> tooltipMap = new HashMap<>();
        tooltipMap.put("show", true);
        pieData.setTooltip(tooltipMap);

        // 颜色元素设置
        Map<String,String> legendMap = new HashMap<>();
        legendMap.put("orient", "vertical");
        legendMap.put("left", "10%"); // 将颜色元素置于左侧
        pieData.setLegend(legendMap);

        EchartsData.Series series = new EchartsData.Series();
        series.setName(name);
        series.setType("pie");
        series.setRadius("50%");
        List<Object> objects = new ArrayList<>();
        for(String key : dataMap.keySet()){
            objects.add(new JSONObject().putOpt("name",key).putOpt("value",dataMap.get(key)));
        }
        series.setData(objects);

        pieData.setSeries(Collections.singletonList(series));
        pieList.add(pieData);
    }

    /**
     * 封装柱状图
     * @param name 标题
     * @param pieList 封装完给前端显示的list
     * @param dataMap 传入的数据
     */
    private void getBarData(String name,List pieList,Map<String,Double> dataMap){
        //标题
        EchartsData barData = new EchartsData();
        Map<String,String> titleMap = new HashMap<>();
        titleMap.put("text", name);
        titleMap.put("left", "center"); // 将标题置于中间
        barData.setTitle(titleMap);

        // 鼠标移动到图形显示信息
        Map<String,Object> tooltipMap = new HashMap<>();
        tooltipMap.put("show", true);
        barData.setTooltip(tooltipMap);

        EchartsData.Series series = new EchartsData.Series();
        series.setName(name);
        series.setType("bar");
        List<Object> objects = new ArrayList<>();
        List<Object> xAxisObjs = new ArrayList<>();
        for(String key : dataMap.keySet()){
            objects.add(dataMap.get(key));
            xAxisObjs.add(key);
        }
        series.setData(objects);
        Map<String,Object> xAxisMap = new HashMap<>();
        xAxisMap.put("data",xAxisObjs);
        barData.setxAxis(xAxisMap);
        barData.setyAxis(new HashMap());

        barData.setSeries(Collections.singletonList(series));
        pieList.add(barData);
    }

}

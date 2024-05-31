package com.javacode.service;


import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javacode.entity.History;
import com.javacode.entity.News;
import com.javacode.mapper.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 历史记录相关的service
 */
@Service
public class HistoryService {

    @Resource
    private HistoryMapper historyMapper;


    /**

     * 新增历史记录
     */
    public History add(History history){
        historyMapper.insertSelective(history);
        return history;
    }
    /**
     * 修改历史记录
     */
    public void update(History history){
        historyMapper.updateByPrimaryKeySelective(history);
    }

    /**
     * 根据id删除历史记录
     */
    public void delete(Long id){
        historyMapper.deleteByPrimaryKey(id);
    }

    /**
     *根据id查询一条历史记录
     */
    public History findById(Long id){
        return historyMapper.selectByPrimaryKey(id);
    }




    @Autowired
    private NewsService newsService;
    public List<String> getUserHistoryKeywords(Long userId) {
        List<String> userHistoryKeywords = new ArrayList<>();
        Set<String> uniqueKeywords = new HashSet<>(); // 使用集合来存储唯一的关键词

        History userHistory = findByUserId(userId); // 根据用户ID获取历史记录
        if (userHistory != null) {
            String newsIdsString = userHistory.getNewsids(); // 获取历史记录中的新闻ID字符串
            if (newsIdsString != null && !newsIdsString.isEmpty()) {
                String[] newsIdsArray = newsIdsString.split(","); // 使用逗号分割字符串
                for (String newsIdStr : newsIdsArray) {
                    try {
                        Long newsId = Long.parseLong(newsIdStr.trim()); // 解析字符串为Long类型
                        News news = newsService.getNewsById(newsId); // 根据新闻ID获取对应的新闻对象
                        if (news != null) {
                            String keywords = news.getKeywords(); // 获取新闻标签
                            if (keywords != null && !keywords.isEmpty()) {
                                String[] keywordArray = keywords.split(","); // 将关键词字符串分割为数组
                                for (String keyword : keywordArray) {
                                    String cleanedKeyword = keyword.trim().toLowerCase(); // 去除空格并转为小写
                                    if (uniqueKeywords.add(cleanedKeyword)) { // 将关键词添加到集合中，如果集合中不存在则添加成功
                                        userHistoryKeywords.add(cleanedKeyword); // 将新闻标签添加到用户历史记录关键字列表中
                                    }
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        // 如果无法解析为Long类型，则跳过该新闻ID
                        System.out.println("Invalid news ID format: " + newsIdStr);
                    }
                }
            }
        }
        return userHistoryKeywords;
    }

    /**
     * 获取用户历史记录关联的新闻ID列表
     * @param userId 用户ID
     * @return 用户历史记录关联的新闻ID列表
     */
    public List<Long> getUserHistoryNewsIds(Long userId) {
        List<Long> userHistoryNewsIds = new ArrayList<>();
        History userHistory = findByUserId(userId); // 根据用户ID获取历史记录
        if (userHistory != null) {
            String newsIdsString = userHistory.getNewsids(); // 获取历史记录中的新闻ID字符串
            if (newsIdsString != null && !newsIdsString.isEmpty()) {
                String[] newsIdsArray = newsIdsString.split(","); // 使用逗号分割字符串
                for (String newsIdStr : newsIdsArray) {
                    try {
                        Long newsId = Long.parseLong(newsIdStr.trim()); // 解析字符串为Long类型
                        userHistoryNewsIds.add(newsId); // 将新闻ID添加到用户历史记录关联的新闻ID列表中
                    } catch (NumberFormatException e) {
                        // 如果无法解析为Long类型，则跳过该新闻ID
                        System.out.println("Invalid news ID format: " + newsIdStr);
                    }
                }
            }
        }
        return userHistoryNewsIds;
    }

    /**
     * 根据用户ID查询历史记录
     * @param userId 用户ID
     * @return 对应用户的历史记录，如果不存在则返回null
     */
    public History findByUserId(Long userId) {
        List<History> historyList = historyMapper.findByUserId(userId);
        if (!historyList.isEmpty()) {
            return historyList.get(0);
        } else {
            return null;
        }
    }


}

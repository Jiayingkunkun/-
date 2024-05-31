package com.javacode.controller;

import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.History;
import com.javacode.service.HistoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 历史记录增删改查控制器
 */
@RestController
@RequestMapping(value = "/history")
public class HistoryController {

    @Resource
    private HistoryService historyService;

    @Value("${spring.upload.path}")
    private String uploadPath;


    /**
     * 接收用户点击记录并存储到历史表
     */
    /**
     * 接收用户点击记录并存储到历史表
     */
    @PostMapping("/recordClick")
    public Result recordClick(@RequestParam Long userId, @RequestParam Long newsId) {
        // 增加调试语句，检查是否正确接收到 userId 参数
        System.out.println("Received userId: " + userId);
        // 检查历史记录中是否存在该用户的记录
        History existingHistory = historyService.findByUserId(userId);

        if (existingHistory == null) {
            // 如果历史记录中不存在该用户的记录，则创建一个新的历史记录对象
            History newHistory = new History();
            newHistory.setUserid(userId);
            // 创建一个新的列表来存储 newsId
            List<Long> newsIdsList = new ArrayList<>();
            if (newsId != null) {
                newsIdsList.add(newsId);
            }
            // 将列表转换为逗号分隔的字符串
            String newsIds = newsIdsList.stream().map(String::valueOf).collect(Collectors.joining(","));
            newHistory.setNewsids(newsIds);
            // 调用 HistoryService 将新的历史记录存储到历史表中
            historyService.add(newHistory);
            return Result.success();
        } else {
            // 如果历史记录中存在该用户的记录，则更新 newsids
            String existingNewsIds = existingHistory.getNewsids();
            if (existingNewsIds == null || !containsNewsId(existingNewsIds, newsId)) {
                // 如果 newsIds 为空或不存在指定的 newsId，则将其添加到历史记录中
                String updatedNewsIds = existingNewsIds == null ? String.valueOf(newsId) : existingNewsIds + "," + newsId;
                existingHistory.setNewsids(updatedNewsIds);
                // 更新历史记录
                historyService.update(existingHistory);
                return Result.success();
            } else {
                return Result.error("3001", "该新闻已经存在于历史记录中。");
            }
        }
    }

    // 检查逗号分隔的字符串中是否包含指定的 newsId
    private boolean containsNewsId(String newsIdsStr, Long newsId) {
        if (newsIdsStr != null && !newsIdsStr.isEmpty()) {
            String[] newsIds = newsIdsStr.split(",");
            for (String id : newsIds) {
                if (id.equals(String.valueOf(newsId))) {
                    return true;
                }
            }
        }
        return false;
    }




    // 检查字符串数组中是否包含指定的newsId
    private boolean containsNewsId(String[] newsIds, Long newsId) {
        for (String id : newsIds) {
            if (id.equals(String.valueOf(newsId))) {
                return true;
            }
        }
        return false;
    }
}
package com.javacode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.News;

import com.javacode.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新闻增删改查控制器
 */
@RestController
@RequestMapping(value = "/news")
public class NewsController {

    @Resource
    private NewsService newsService;

    @Value("${spring.upload.path}")
    private String uploadPath;

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<Result<String>> uploadImage(@PathVariable Long id,
                                                      @RequestParam("image") MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.error("1006","图片文件为空"));
        }

        try {
            // 生成唯一的文件名，包含时间戳
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String fileName = timeStamp + "_" + imageFile.getOriginalFilename();

            // 保存图片文件到服务器的指定目录
            Path uploadDir = Paths.get(uploadPath);
            // 注意这里路径拼接的修改，不再包含硬编码的 /file/ 前缀
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 更新数据库中对应的 news 记录的 picture 字段
            News news = newsService.findById(id);
            if (news != null) {
                news.setPicture(fileName); // 将文件名存入数据库
                newsService.update(news);
                return ResponseEntity.ok().body(Result.success(fileName));

            } else {
                Files.delete(filePath); // 删除上传的图片文件
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Result.error("1007","找不到对应的新闻记录"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Result.error("1008","图片上传失败"));
        }
    }

    /**
     * 分页查询新闻列表
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @param title 新闻名字
     */
    @GetMapping("/page/{title}")
    public Result<PageInfo<News>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @PathVariable String title){
        return Result.success(newsService.findPage(pageNum,pageSize,title));
    }

    /**
     * 新增新闻
     */
    @PostMapping
    public Result<News> add(@RequestBody News news){
        // 添加调试语句，用于检查接收到的新闻对象
        System.out.println("Received news object: " + news);

        // 添加调试语句，用于检查接收到的新闻标题和内容
        System.out.println("Received news title: " + news.getTitle());
        System.out.println("Received news content: " + news.getContent());

        newsService.add(news);
        return Result.success(news);
    }


    /**
     * 更新新闻
     */
    @PutMapping
    public Result update(@RequestBody News news){
        newsService.update(news);
        return Result.success();
    }

    /**
     * 删除新闻
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        newsService.delete(id);
        return Result.success();
    }

    /**
     * 根据id查询一条新闻
     */
    @GetMapping ("/{id}")
    public Result detail(@PathVariable Long id){
        return Result.success(newsService.findById(id));
    }
    //获取内容
    @GetMapping("/content/{id}")
    public Result<String> getContent(@PathVariable Long id) {
        News news = newsService.findById(id);
        if (news != null) {
            return Result.success(news.getContent());
        } else {
            return Result.error();
        }
    }
    /**
     * 更新点击次数
     * 浏览量
     */
    @PostMapping("/{id}/click")
    public ResponseEntity<Void> updateClickCount(@PathVariable Long id) {
        newsService.incrementClickCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新用户点赞新闻
     * @param id 新闻ID
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeNews(@PathVariable Long id) {
        // 根据ID查询新闻
        News news = newsService.findById(id);
        if (news != null) {
            // 更新点赞数
            newsService.incrementLikeCount(id);
            return ResponseEntity.ok().body("Liked successfully"); // 返回一个非空字符串作为成功响应
        } else {
            // 如果找不到对应的新闻记录，返回404
            return ResponseEntity.notFound().build();
        }
    }



    /**
     * 获取新闻的浏览数
     */
    @GetMapping("/{id}/clickCount")
    public ResponseEntity<Integer> getClickCount(@PathVariable Long id) {
        int clickCount = newsService.getClickCount(id);
        return ResponseEntity.ok(clickCount);
    }

    /**
     * 获取新闻的点赞数
     */
    @GetMapping("/{id}/likeCount")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long id) {
        int likeCount = newsService.getLikeCount(id);
        return ResponseEntity.ok(likeCount);
    }


    // 获取所有新闻信息
    @GetMapping("/all")
    public List<News> getAllNewss() {
        // 在这里从数据库中获取所有的新闻信息
        List<News> newss = newsService.getAllNewss();
        return newss;
    }
    /**
     * 根据id查询一条新闻的标题和内容
     */
    @GetMapping("/title-and-content/{id}")
    public Result<Map<String, String>> detailTitleAndContent(@PathVariable Long id){
        News news = newsService.findById(id);
        if (news != null) {
            Map<String, String> titleAndContent = new HashMap<>();
            titleAndContent.put("title", news.getTitle());
            titleAndContent.put("content", news.getContent());
//            System.out.println("标题和内容：" + titleAndContent); // 添加日志
            return Result.success(titleAndContent);
        } else {
            return Result.error("1009", "找不到对应的新闻记录");
        }
    }
    /**
     * 保存新闻类型
     * @param id 新闻ID
     */
    @PutMapping("/saveType/{id}")
    public Result saveNewsType(@PathVariable Long id, @RequestBody String requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 解析请求体中的 JSON
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            // 提取出类型字段
            String type = jsonNode.get("type").asText();

            // 根据ID查询新闻
            News news = newsService.findById(id);
            if (news != null) {
                // 更新新闻类型
                System.out.println(type);
                news.setType(type);
                newsService.update(news);
                return Result.success();
            } else {
                // 如果找不到对应的新闻记录，返回错误信息
                return Result.error("1010", "找不到对应的新闻记录");
            }
        } catch (IOException e) {
            // JSON 解析失败
            e.printStackTrace();
            return Result.error("500", "服务器内部错误");
        }
    }

    /**
     * 更新新闻类型
     * @param newsId 新闻ID
     * @param type 新的新闻类型
     * @return ResponseEntity
     */
    @PutMapping("/updateType/{newsId}")
    public ResponseEntity<Result<Void>> updateNewsType(@PathVariable Long newsId, @RequestParam String type) {
        // 根据新闻ID查询新闻信息
        News news = newsService.findById(newsId);
        // 添加调试语句以检查参数值
        System.out.println("Received type parameter: " + type);
        if (news == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Result.error("404", "找不到对应的新闻记录"));
        }

        // 更新新闻类型
        news.setType(type); // 将接收到的类型直接存储到数据库中
        newsService.update(news);

        return ResponseEntity.ok().body(Result.success());
    }
    //用户页面新闻搜索
    @GetMapping("/search/news")
    public ResponseEntity<List<News>> searchByTitle(@RequestParam String title) {
        List<News> newsList = newsService.findByTitle(title);
        return ResponseEntity.ok(newsList);
    }

}
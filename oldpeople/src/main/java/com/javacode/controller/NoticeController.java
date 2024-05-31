package com.javacode.controller;

import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.Notice;
import com.javacode.service.NoticeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 公告增删改查控制器
 */
@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

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

            // 更新数据库中对应的 notice 记录的 picture 字段
            Notice notice = noticeService.findById(id);
            if (notice != null) {
                notice.setPicture(fileName); // 将文件名存入数据库
                noticeService.update(notice);
                return ResponseEntity.ok().body(Result.success(fileName));

            } else {
                Files.delete(filePath); // 删除上传的图片文件
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Result.error("1007","找不到对应的公告记录"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Result.error("1008","图片上传失败"));
        }
    }

    /**
     * 分页查询公告列表
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @param title 公告名字
     */
    @GetMapping("/page/{title}")
    public Result<PageInfo<Notice>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @PathVariable String title) {
        // 添加调试语句以输出接收到的参数值
        System.out.println("接收到的参数 title: " + title);
        System.out.println("接收到的参数 pageNum: " + pageNum);
        System.out.println("接收到的参数 pageSize: " + pageSize);

        // 在此处将 title 参数转换为整数，或者根据业务逻辑处理
        // 示例中假设 title 为字符串，直接传递给 service 层处理
        return Result.success(noticeService.findPage(pageNum, pageSize, title));
    }


    /**
     * 分页查询公告列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页后的公告列表
     */
    @GetMapping("/page")
    public Result<PageInfo<Notice>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Notice> pageInfo = noticeService.findPages(pageNum, pageSize);
        // 添加调试语句，输出返回的分页信息
        System.out.println("成功获取到分页信息：" + pageInfo.toString());
        return Result.success(pageInfo);
    }


    /**
     * 新增公告
     */
    @PostMapping
    public Result<Notice> add(@RequestBody Notice notice){
        noticeService.add(notice);
        return Result.success(notice);
    }

    /**
     * 更新公告
     */
    @PutMapping
    public Result update(@RequestBody Notice notice){
        noticeService.update(notice);
        return Result.success();
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        noticeService.delete(id);
        return Result.success();
    }

    /**
     * 根据id查询一条公告
     */
    @GetMapping ("/{id}")
    public Result detail(@PathVariable Long id){
        return Result.success(noticeService.findById(id));
    }
    //获取内容
    @GetMapping("/content/{id}")
    public Result<String> getContent(@PathVariable Long id) {
        Notice notice = noticeService.findById(id);
        if (notice != null) {
            return Result.success(notice.getContent());
        } else {
            return Result.error();
        }
    }
    /**
     * 更新点击次数
     */
    @PostMapping("/{id}/click")
    public ResponseEntity<Void> updateClickCount(@PathVariable Long id) {
        noticeService.incrementClickCount(id);
        return ResponseEntity.ok().build();
    }

    // 获取所有公告信息
    @GetMapping("/all")
    public List<Notice> getAllNotices() {
        // 在这里从数据库中获取所有的公告信息
        List<Notice> notices = noticeService.getAllNotices();
        return notices;
    }
    /**
     * 根据id查询一条公告的标题和内容
     */
    @GetMapping("/title-and-content/all")
    public Result<Map<String, String>> detailTitleAndContent(@PathVariable Long id){
        Notice notice = noticeService.findById(id);
        if (notice != null) {
            Map<String, String> titleAndContent = new HashMap<>();
            titleAndContent.put("title", notice.getTitle());
            titleAndContent.put("content", notice.getContent());
            System.out.println("标题和内容：" + titleAndContent); // 添加日志
            return Result.success(titleAndContent);
        } else {
            return Result.error("1009", "找不到对应的公告记录");
        }
    }




}
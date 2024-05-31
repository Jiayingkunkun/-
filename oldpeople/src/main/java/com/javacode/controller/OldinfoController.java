package com.javacode.controller;

import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.Oldinfo;
import com.javacode.service.OldinfoService;
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

/**
 * 老人增删改查控制器
 */
@RestController
@RequestMapping(value = "/oldinfo")
public class OldinfoController {

    @Resource
    private OldinfoService oldinfoService;
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
            Oldinfo oldinfo = oldinfoService.findById(id);
            if (oldinfo != null) {
                oldinfo.setPicture(fileName); // 将文件名存入数据库
                oldinfoService.update(oldinfo);
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
     * 分页查询老人列表
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @param name 老人名字
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<Oldinfo>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "15") Integer pageSize,
                                       @PathVariable String name){
        return Result.success(oldinfoService.findPage(pageNum,pageSize,name));
    }

    /**
     * 新增老人
     */
    //@PutMapping用于修改
    @PostMapping
    public Result<Oldinfo> add(@RequestBody Oldinfo oldinfo){
        oldinfoService.add(oldinfo);  //调用service层的方法
        return Result.success(oldinfo);
    }

    /**
     * 更新老人
     */
    @PutMapping
    public Result update(@RequestBody Oldinfo oldinfo){
        oldinfoService.update(oldinfo);  //调用service层的方法
        return Result.success();
    }

    /**
     * 删除老人
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        oldinfoService.delete(id);
        return Result.success();
    }

    /**
     * 根据id查询一个老人信息
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id){
        return Result.success(oldinfoService.findById(id));
    }

//    /**
//     * 根据老人ID查询老人的详细信息
//     */
//    @GetMapping("/detail/{id}")
//    public Result detail(@PathVariable Long id){
//        Oldinfo oldinfo = oldinfoService.findById(id);
//        if (oldinfo != null) {
//            return Result.success(oldinfo);
//        } else {
//            return Result.error("1001", "找不到对应的老人信息");
//        }
//    }

}

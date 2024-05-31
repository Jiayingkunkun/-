package com.javacode.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.javacode.common.Result;
import com.javacode.entity.Fileinfo;
import com.javacode.exception.CustomException;
import com.javacode.service.FileinfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

/**
 * 文件增删改查控制器
 */
@RestController
@RequestMapping(value = "/fileinfo")
public class FileinfoController {

    private static final String BASE_PATH = "src/main/resources/static/file/"; // 相对路径

    @Resource
    private FileinfoService fileinfoService;

    /**
     * 上传文件
     */
//@PutMapping用于修改
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        if(originalName == null){
            return Result.error("1001","文件名不能为空");
        }
        if(originalName.contains("png") && originalName.contains("jpg") && originalName.contains("jpeg") && originalName.contains("gif")){
            return Result.error("1002","只能上传图片");
        }
        //文件名加时间戳，防止文件名一样但图片不一样
        String fileName = FileUtil.mainName(originalName)+System.currentTimeMillis()+"."+ FileUtil.extName(originalName);
        //文件上传
        FileUtil.writeBytes(file.getBytes(),BASE_PATH+fileName);
        //信息入库，获取文件id
        Fileinfo fileinfo = new Fileinfo();
        fileinfo.setOriginname(originalName);
        fileinfo.setFilename(fileName);
        Fileinfo addInfo = fileinfoService.add(fileinfo);
        if(addInfo!=null){
            return Result.success(addInfo);
        }
        return Result.error("1003","上传失败");
    }



    /**
     * 删除文件
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        fileinfoService.delete(id);
        return Result.success();
    }

    /**
     * 根据id查询一个文件记录
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id){
        return Result.success(fileinfoService.findById(id));
    }

    /**
     * 下载文件图片
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response) throws IOException {
        if(StrUtil.isBlank(id) || "null".equals(id)){
            throw new CustomException("1001","未上传文件");
        }
        Fileinfo fileinfo = fileinfoService.findById(Long.parseLong(id));
        if(fileinfo==null){
            throw new CustomException("1002","未找到文件");
        }
        byte[] bytes = FileUtil.readBytes(BASE_PATH+fileinfo.getFilename());
        response.reset();
        response.addHeader("Content-Disposition","attachment;filename="+
                URLEncoder.encode(fileinfo.getOriginname(),"UTF-8"));
        response.addHeader("Content-Length",""+bytes.length);
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        toClient.write(bytes);
        toClient.flush();
        toClient.close();
    }
}

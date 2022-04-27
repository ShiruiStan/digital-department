package com.atcdi.digital.controller;

import com.atcdi.digital.entity.StandardResponse;
import com.atcdi.digital.entity.UploadFile;
import com.atcdi.digital.service.FileService;
import io.swagger.annotations.Api;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
@Api(tags = "文件相关接口")
public class FileController {
    @Resource
    FileService fileService;

    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable("id") int id, @NonNull String token, HttpServletResponse response) {
        UploadFile file = fileService.getFileById(id ,token);
        response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8));
        response.setHeader("X-Accel-Redirect", "/department/document" + URLEncoder.encode(file.getFilePath() + file.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+","%20"));
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setHeader("Expires", "0");
    }

    @GetMapping("/list")
    public StandardResponse getAllFiles(){
        return StandardResponse.success(fileService.getAllFiles());
    }

    @PostMapping("/common")
    public StandardResponse uploadCommonFile(){
        return StandardResponse.success("上传成功");
    }

    @DeleteMapping("/delete")
    public StandardResponse deleteFiles(@NonNull int id){
        fileService.deleteFile(id);
        return StandardResponse.success();
    }

}

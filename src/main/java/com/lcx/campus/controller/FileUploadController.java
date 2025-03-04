package com.lcx.campus.controller;

import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.utils.FileUploadUtils;
import com.lcx.campus.utils.MimeTypeUtils;
import jakarta.annotation.Resource;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class FileUploadController {
    @Resource
    private FileStorageService fileStorageService;

    @PostMapping("/image")
    public Result uploadImage(@RequestParam("image") MultipartFile file) {
        if(file.isEmpty()) {
            return Result.fail("上传失败，请选择文件");
        }
        String imgUrl = FileUploadUtils.upload("image/", file, MimeTypeUtils.IMAGE_EXTENSION, fileStorageService);
        return Result.success(imgUrl);
    }

    @PostMapping("/avatar")
    public Result uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        if(file.isEmpty()) {
            return Result.fail("上传失败，请选择文件");
        }
        String imgUrl = FileUploadUtils.upload("avatar/", file, MimeTypeUtils.IMAGE_EXTENSION, fileStorageService);
        return Result.success(imgUrl);
    }
}

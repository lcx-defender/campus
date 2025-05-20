package com.lcx.campus.controller;

import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.service.IUserService;
import com.lcx.campus.utils.FileUploadUtils;
import com.lcx.campus.utils.MimeTypeUtils;
import com.lcx.campus.utils.SecurityUtils;
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
    @Resource
    private IUserService userService;

    @PostMapping("/image")
    public Result uploadImage(@RequestParam("image") MultipartFile file) {
        if(file.isEmpty()) {
            return Result.fail("上传失败，请选择文件");
        }
        String imgUrl = FileUploadUtils.upload("image/", file, MimeTypeUtils.IMAGE_EXTENSION, fileStorageService);
        return Result.success(imgUrl);
    }

    @PostMapping("/avatar")
    @Log(title = "上传头像", businessType = BusinessType.OTHER)
    public Result uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        if(file.isEmpty()) {
            return Result.fail("上传失败，请选择文件");
        }
        String imgUrl = FileUploadUtils.upload("avatar/", file, MimeTypeUtils.IMAGE_EXTENSION, fileStorageService);
        User user = new User();
        user.setAvatar(imgUrl);
        user.setUserId(SecurityUtils.getUserId());
        boolean isSuccess = userService.updateById(user);
        return isSuccess ? Result.success(imgUrl) : Result.fail("上传失败");
    }
}

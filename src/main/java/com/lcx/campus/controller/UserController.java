package com.lcx.campus.controller;


import com.lcx.campus.domain.dto.LoginBody;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    /**
     * 获取当前登录用户个人信息
     */
    @GetMapping("/getSelfInfo")
    public Result getSelfInfo() {
        return userService.getSelfInfo();
    }
}

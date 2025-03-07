package com.lcx.campus.controller;


import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.LoginForm;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login/username")
    public Result loginByUsername(@RequestBody LoginForm loginForm) {
        return userService.loginByUsername(loginForm);
    }
}

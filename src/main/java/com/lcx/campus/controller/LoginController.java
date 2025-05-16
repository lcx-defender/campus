package com.lcx.campus.controller;

import com.lcx.campus.domain.dto.LoginBody;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.IMenuService;
import com.lcx.campus.service.IUserService;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     登录模块接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-04-07
 */
@RestController
public class LoginController {
    @Resource
    private IUserService userService;
    @Resource
    private IMenuService menuService;

    /**
     * 根据用户名(身份证号、手机号 或 邮箱)和密码登录方式
     * @return 登录令牌
     */
    @PostMapping("/loginByUsername")
    public Result loginByUsername(@RequestBody LoginBody loginBody) {
        return userService.loginByUsername(loginBody);
    }

    /**
     * 学生特有根据学号和密码登录方式
     */
    @PostMapping("/loginByStudentId")
    public Result loginByStudentId(@RequestBody LoginBody loginBody) {
        return userService.loginByStudentId(loginBody);
    }

    /**
     * 获取登录用户的前端路由
     */
    @GetMapping("/getRouters")
    public Result getRouters() {
        Long userId = SecurityUtils.getUserId();
        return menuService.getRouters(userId);
    }
}

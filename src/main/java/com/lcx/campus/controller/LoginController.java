package com.lcx.campus.controller;

import com.lcx.campus.domain.dto.LoginBody;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.IUserService;
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

    /**
     * 根据用户名和密码登录方式
     * @param loginBody 登录提交表单
     * @return 登录令牌
     */
    @PostMapping("/loginByUsername")
    public Result loginByUsername(@RequestBody LoginBody loginBody) {
        return userService.loginByUsername(loginBody);
    }
}

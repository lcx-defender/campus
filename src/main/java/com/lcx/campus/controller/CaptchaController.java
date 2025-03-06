package com.lcx.campus.controller;

import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.impl.CaptchaServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     验证码操作处理
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-06
 */
@RestController
public class CaptchaController {

    @Resource
    private CaptchaServiceImpl captchaService;

    @GetMapping("/captchaImage")
    public Result getCode(HttpServletResponse response) {
        return captchaService.getCode(response);
    }
}

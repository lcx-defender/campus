package com.lcx.campus.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误导致登录失败异常类
 *
 * @author 刘传星
 * @since 2025-04-30
 */
public class WrongCaptchaCodeException extends AuthenticationException {

    public WrongCaptchaCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public WrongCaptchaCodeException(String msg) {
        super(msg);
    }
}

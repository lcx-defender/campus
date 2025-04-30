package com.lcx.campus.handler;

import com.lcx.campus.constant.Constants;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.utils.AsyncFactory;
import com.lcx.campus.utils.AsyncManager;
import com.lcx.campus.utils.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.lcx.campus.constant.HttpStatus.FORBIDDEN;
import static com.lcx.campus.constant.HttpStatus.UNAUTHORIZED;

/**
 * 全局异常处理
 *
 * @since 2025-03-06
 * @author 刘传星
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public Result handleAuthenticationException(AuthenticationException e) {
        e.printStackTrace();
        return Result.fail(UNAUTHORIZED, "认证失败: " + (StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "未知错误"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e) {
        e.printStackTrace();
        return Result.fail(FORBIDDEN ,"权限不足: " + (StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "未知错误"));
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        AsyncManager.me().execute(AsyncFactory.recordLoginInfo(SecurityUtils.getLoginUser().getUserId(), Constants.LOGIN_FAIL, "用户不存在/密码错误"));
        return Result.fail(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败");
    }
}

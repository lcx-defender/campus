package com.lcx.campus.handler;

import com.alibaba.fastjson2.JSON;
import com.lcx.campus.constant.HttpStatus;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.utils.ServletUtils;
import com.lcx.campus.utils.StringUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 权限校验失败处理类
 *
 * @author 刘传星
 * @since 2025-05-01
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Integer code = HttpStatus.FORBIDDEN;
        String msg = StringUtils.format("请求访问：{}，访问权限不足，请联系管理员", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(Result.fail(code, msg)));
    }
}

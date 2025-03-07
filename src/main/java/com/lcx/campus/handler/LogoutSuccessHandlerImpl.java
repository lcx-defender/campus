package com.lcx.campus.handler;

import com.alibaba.fastjson2.JSON;
import com.lcx.campus.constant.Constants;
import com.lcx.campus.domain.dto.LoginUser;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.impl.JwtTokenServiceImpl;
import com.lcx.campus.utils.AsyncFactory;
import com.lcx.campus.utils.AsyncManager;
import com.lcx.campus.utils.ServletUtils;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Resource
    private JwtTokenServiceImpl jwtTokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        LoginUser loginUser = jwtTokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            jwtTokenService.delLoginUser(loginUser.getTokenUUID());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(Long.valueOf(userName), Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(Result.success("退出成功")));
    }
}

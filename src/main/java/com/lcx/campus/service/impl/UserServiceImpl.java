package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcx.campus.constant.Constants;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.LoginForm;
import com.lcx.campus.domain.dto.LoginUser;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.mapper.UserMapper;
import com.lcx.campus.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.AsyncFactory;
import com.lcx.campus.utils.AsyncManager;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 通过登陆界面用户名查询用户信息
     * @param username
     * @return
     */
    @Override
    public User selectUserByUserName(String username) {
        // 查询规则，通过userId、phone、email、identity进行精确查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", username).or().eq("phone", username).or().eq("email", username).or().eq("identity", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Result login(LoginForm loginForm) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(authenticationToken);
        if (authentication.isAuthenticated()) {

        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        AsyncManager.me().execute(AsyncFactory.recordLoginInfo(loginUser.getUserId(), Constants.LOGIN_SUCCESS, "登录成功"));

        return null;
    }
}

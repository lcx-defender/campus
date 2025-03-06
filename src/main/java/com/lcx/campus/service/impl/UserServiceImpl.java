package com.lcx.campus.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcx.campus.constant.Constants;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.LoginForm;
import com.lcx.campus.domain.dto.LoginUser;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.exception.BaseException;
import com.lcx.campus.mapper.UserMapper;
import com.lcx.campus.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.AsyncFactory;
import com.lcx.campus.utils.AsyncManager;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.lcx.campus.constant.RedisConstants.CAPTCHA_CODE_KEY;

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
    private UserDetailsService userDetailsService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JwtTokenServiceImpl jwtTokenService;

    /**
     * 通过登陆界面用户名查询用户信息
     * @param username 身份证号、手机号、邮箱
     * @return
     */
    @Override
    public User selectUserByUserName(String username) {
        // 查询规则，通过userId、phone、email、identity进行精确查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("identity", username).or().eq("phone", username).or().eq("email", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Result login(LoginForm loginForm) {
        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(loginForm.getUsername());
        validateCaptcha(loginUser.getUserId(), loginForm.getCode(), loginForm.getUuid());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (authentication.isAuthenticated()) {

        }
        loginUser = (LoginUser) authentication.getPrincipal();
        AsyncManager.me().execute(AsyncFactory.recordLoginInfo(loginUser.getUserId(), Constants.LOGIN_SUCCESS, "登录成功"));
        return Result.success(jwtTokenService.createToken(loginUser));
    }

    private void validateCaptcha(Long userId, String code, String uuid) {
        String key = CAPTCHA_CODE_KEY + uuid;
        String cacheCode = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(cacheCode)) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(userId, Constants.LOGIN_FAIL, "验证码已过期"));
            throw new BaseException("验证码已过期");
        }
        stringRedisTemplate.delete(key);
        if (!StrUtil.equals(cacheCode, code)) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(userId, Constants.LOGIN_FAIL, "验证码错误"));
            throw new BaseException("验证码错误");
        }
    }
}

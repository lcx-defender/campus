package com.lcx.campus.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lcx.campus.constant.Constants;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.LoginBody;
import com.lcx.campus.domain.dto.LoginUser;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.exception.BaseException;
import com.lcx.campus.mapper.UserMapper;
import com.lcx.campus.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.AddressUtils;
import com.lcx.campus.utils.AsyncFactory;
import com.lcx.campus.utils.AsyncManager;
import com.lcx.campus.utils.IpUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
     *
     * @param username 身份证号、手机号、邮箱
     * @return
     */
    @Override
    public User selectUserByUserName(String username) {
        return userMapper.selectUserByUserName(username);
    }

    /**
     * @param loginBody
     * @return 返回Result携带Jwt令牌
     */
    @Override
    public Result loginByUsername(LoginBody loginBody) {
        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(loginBody.getUsername());
        // 验证码校验
        validateCaptcha(loginUser.getUserId(), loginBody.getCode(), loginBody.getUuid());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginBody.getUsername(), loginBody.getPassword(), loginUser.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        loginUser = (LoginUser) authentication.getPrincipal();
        AsyncManager.me().execute(AsyncFactory.recordLoginInfo(loginUser.getUserId(), Constants.LOGIN_SUCCESS, "登录成功"));
        recordLoginInfo(loginUser.getUserId());
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

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        User sysUser = new User();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginLocation(AddressUtils.getRealAddressByIP(sysUser.getLoginIp()));
        sysUser.setUpdateTime(LocalDateTime.now());
        updateUserProfile(sysUser);
    }

    @Override
    public Result updateUserProfile(User user) {
        boolean isSuccess = updateById(user);
        return isSuccess ? Result.success("更新用户信息成功") : Result.fail("更新用户信息失败");
    }
}

package com.lcx.campus.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.constant.Constants;
import com.lcx.campus.domain.Role;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.LoginBody;
import com.lcx.campus.domain.dto.LoginUser;
import com.lcx.campus.domain.dto.PasswordBody;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.domain.vo.UserRolesVo;
import com.lcx.campus.exception.BaseException;
import com.lcx.campus.exception.WrongCaptchaCodeException;
import com.lcx.campus.mapper.*;
import com.lcx.campus.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.lcx.campus.constant.Constants.LOGIN_SUCCESS;
import static com.lcx.campus.constant.RedisConstants.CAPTCHA_CODE_KEY;
import static com.lcx.campus.constant.UserConstants.DEFAULT_AVATAR;
import static com.lcx.campus.constant.UserConstants.SYS_USER;

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
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private StudentMapper studentMapper;

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
     * 根据用户名(身份证号、手机号 或 邮箱)和密码登录方式
     * @return 登录令牌
     */
    @Override
    public Result loginByUsername(LoginBody loginBody) {
        return login(loginBody);
    }

    @Override
    public Result loginByStudentId(LoginBody loginBody) {
        if(StringUtils.isNull(loginBody.getStudentId()) || StringUtils.isNull(loginBody.getPassword())) {
            return Result.fail("学号或密码不能为空");
        }
        // 根据学号查询用户
        User user = userMapper.selectUserByStudentId(loginBody.getStudentId());
        if(StringUtils.isNull(user)) {
            return Result.fail("用户不存在");
        }
        loginBody.setUsername(user.getIdentity());
        return login(loginBody);
    }

    private Result login(LoginBody loginBody) {
        LoginUser loginUser = (LoginUser) userDetailsService.loadUserByUsername(loginBody.getUsername());
        validateCaptcha(loginUser.getUserId(), loginBody.getCode(), loginBody.getUuid());
        // security 认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginBody.getUsername(), loginBody.getPassword(), loginUser.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        loginUser = (LoginUser) authentication.getPrincipal();
        AsyncManager.me().execute(AsyncFactory.recordLoginInfo(loginUser.getUserId(), LOGIN_SUCCESS, "登录成功"));
        recordLoginInfo(loginUser.getUserId());
        // 生成token并返回
        return Result.success(jwtTokenService.createToken(loginUser));
    }

    /**
     * 校验用户登录提交的验证码
     */
    private void validateCaptcha(Long userId, String code, String uuid) {
        String key = CAPTCHA_CODE_KEY + uuid;
        String cacheCode = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(cacheCode)) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(userId, Constants.LOGIN_FAIL, "验证码已过期"));
            throw new BaseException("验证码已过期");
        }
        stringRedisTemplate.delete(key);
        // 比较验证码时，希望忽略大小写
        if (!code.equalsIgnoreCase(cacheCode)) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(userId, Constants.LOGIN_FAIL, "验证码错误,应为" + cacheCode + ",提交为" + code));
            throw new WrongCaptchaCodeException("验证码错误");
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
    /**
     * 获取当前登录用户的信息
     *
     * @return 当前登录用户账户信息
     */
    @Override
    public Result getSelfInfo() {
        Long userId = SecurityUtils.getUserId();

        if (Objects.isNull(userId)) {
            return Result.fail("获取用户信息失败");
        }
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            return Result.fail("用户不存在");
        }
        // 返回用户基本信息，对密码进行脱敏处理
        user.setPassword("********");
        return Result.success(user);
    }

    /**
     * 更新当前登录用户的密码
     *
     * @param passwordBody
     * @param request
     * @return 更改结果
     */
    @Override
    public Result updatePassword(PasswordBody passwordBody, HttpServletRequest request) {
        Long userId = SecurityUtils.getUserId();
        String oldPassword = passwordBody.getOldPassword();
        String newPassword = passwordBody.getNewPassword();
        String confirmPassword = passwordBody.getConfirmPassword();
        // 旧密码校验
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            return Result.fail("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.fail("旧密码错误");
        }
        // 更新密码
        if (!newPassword.equals(confirmPassword)) {
            return Result.fail("两次输入密码不一致");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return Result.fail("新密码不能与旧密码相同");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        boolean isSuccess = updateById(user);
        if (!isSuccess) {
            return Result.success("更新密码失败");
        }
        // TODO 如何退出已经登录的其他当前用户(多端登录)
        // 更新完之后需要退出，重新登录
        LoginUser loginUser = jwtTokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            jwtTokenService.delLoginUser(loginUser.getTokenUUID());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(Long.valueOf(userName), Constants.LOGOUT, "退出成功"));
        }
        return Result.success("更改成功，请重新登录", null);
    }

    /**
     * 管理员重置用户密码
     *
     * @param passwordBody
     * @return
     */
    @Override
    public Result resetPassword(PasswordBody passwordBody) {
        User user = new User();
        user.setUserId(passwordBody.getUserId());
        user.setPassword(passwordEncoder.encode(passwordBody.getNewPassword()));
        boolean isSuccess = updateById(user);
        return isSuccess ? Result.success("重置密码成功") : Result.fail("重置密码失败");
    }

    /**
     * 获取用户ID:若用户已经存在则返回用户ID，否则插入用户信息并返回用户ID
     *
     * @param user
     * @return 用户ID
     */
    public Long creatUserIfNotExist(User user) {
        // 1. 校验用户是否存在
        User existingUser = lambdaQuery()
                .eq(User::getIdentity, user.getIdentity())
                .one();
        Long userId = null;
        boolean isSuccess = false;
        if (existingUser == null) {
            // 2. 用户不存在，插入用户
            if(StringUtils.isEmpty(user.getAvatar())) {
                user.setAvatar(DEFAULT_AVATAR);
            }
            if(StringUtils.isEmpty(user.getNickname())) {
                user.setNickname(SYS_USER + UUID.randomUUID().toString().substring(0, 8));
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreateTime(LocalDateTime.now());
            isSuccess = save(user);
            userId = user.getUserId();
        }
        // 1. 用户存在，返回null，新增失败
        return isSuccess ? userId : null;
    }

    /**
     * 用户修改个人信息，只允许个人修改 nickname、email、phone、sex 这四个字段，根据当前登录token更新
     */
    @Override
    public Result updateSelfInfo(User user) {
        User currentUser = new User();
        currentUser.setUserId(SecurityUtils.getUserId());
        currentUser.setNickname(user.getNickname());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhone(user.getPhone());
        currentUser.setSex(user.getSex());
        return updateUser(currentUser);
    }

    @Override
    public Result updateUser(User user) {
        return updateById(user) ? Result.success("更新用户信息成功", null) : Result.fail("更新用户信息失败");
    }

    /**
     * 分页查询所有自己部门之下的用户信息
     * 通过用户昵称、用户类型、邮箱、手机号、用户状态去多条件查询
     */
    @Override
    public Result pageUserList(User user) {
        Page<User> queryPage = user.toMpPage();
        Page<User> page = lambdaQuery()
                .like(user.getNickname() != null, User::getNickname, user.getNickname())
                .eq(user.getUserType() != null, User::getUserType, user.getUserType())
                .eq(user.getEmail() != null, User::getEmail, user.getEmail())
                .eq(user.getPhone() != null, User::getPhone, user.getPhone())
                .eq(user.getUserStatus() != null, User::getUserStatus, user.getUserStatus())
                .page(queryPage);
        PageVo<User> res = PageVo.of(page);
        return Result.success("查询用户列表成功", res);
    }

    /**
     * 管理员修改用户角色信息
     */
    @Override
    public Result updateUserRoles(UserRolesVo userRolesVo) {
        // 1. 校验用户是否存在
        User user = userMapper.selectById(userRolesVo.getUserId());
        if(StringUtils.isNull(user)) {
            return Result.fail("用户不存在");
        }
        // 对roleIds进行去重
        Long[] roleIds = Arrays.stream(userRolesVo.getRoleIds())
                .distinct()
                .toArray(Long[]::new);
        if(roleIds.length == 0) {
            boolean isSuccess = userRoleMapper.deleteByUserId(userRolesVo.getUserId());
            return isSuccess ? Result.success("删除用户角色成功") : Result.fail("删除用户角色失败");
        }
        // 2. 校验角色是否存在
        List<Role> roles = roleMapper.selectBatchIds(Arrays.asList(userRolesVo.getRoleIds()));
        if(roles.size() != roleIds.length) {
            return Result.fail("有角色不存在");
        }
        // 3. 删除用户原有角色
        List<Role> roleList = roleMapper.selectRoleByUserId(userRolesVo.getUserId()); // 查询角色原有的菜单
        boolean isSuccess = userRoleMapper.deleteByUserId(userRolesVo.getUserId());
        if(!isSuccess && roleList.size() > 0) {
            return Result.fail("删除用户原有角色失败");
        }
        // 4. 添加用户新角色
        int rows = userRoleMapper.insertBatch(userRolesVo.getUserId(), roleIds);
        return rows == roles.size() ? Result.success("修改用户角色成功") : Result.fail("修改用户角色失败");
    }
    @Override
    public Result addUserOfAdmin(User user) {
        // 1. 校验用户是否存在
        Long userId = creatUserIfNotExist(user);
        if (StringUtils.isNull(userId)) {
            return Result.fail("管理员用户添加失败，请检查用户信息是否完整或已存在");
        }
        return Result.success("添加用户成功", userId);
    }
}

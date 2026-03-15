package com.lcx.campus.service.impl;

import com.lcx.campus.domain.po.User;
import com.lcx.campus.domain.dto.LoginUser;
import com.lcx.campus.enums.UserStatus;
import com.lcx.campus.mapper.UserMapper;
import com.lcx.campus.service.IMenuService;
import com.lcx.campus.service.IRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-05
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private IMenuService menuService;
    @Resource
    private IRoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectUserByUserName(username);
        if(user == null) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("用户不存在");
        }
        if (user.getUserStatus().equals(UserStatus.DISABLE.getCode())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new UsernameNotFoundException("用户已被停用");
        }
        if (user.getUserStatus().equals(UserStatus.DELETED.getCode())) {
            log.info("登录用户：{} 已注销.", username);
            throw new UsernameNotFoundException("用户已注销");
        }
        user.setMenus(menuService.selectMenuByUserId(user.getUserId()));
        user.setRoles(roleService.selectRoleByUserId(user.getUserId()));
        return createLoginUser(user);
    }

    private UserDetails createLoginUser(User user) {
        // 查询用户权限菜单
        Set<String> permissions = menuService.selectMenuPermsByUserId(user.getUserId());
        return new LoginUser(user, permissions);
    }
}

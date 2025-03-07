package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.LoginUser;
import com.lcx.campus.enums.UserStatus;
import com.lcx.campus.mapper.MenuMapper;
import com.lcx.campus.mapper.UserMapper;
import com.lcx.campus.service.IMenuService;
import com.lcx.campus.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.util.validation.metadata.NamedObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

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
    private MenuMapper menuMapper;

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
        return createLoginUser(user);
    }

    private UserDetails createLoginUser(User user) {
        // 查询用户权限菜单
        List<String> perms = menuMapper.selectMenuPermsByUserId(user.getUserId());
        Set<String> permissions = Set.copyOf(perms);
        return new LoginUser(user, permissions);
    }
}

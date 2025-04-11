package com.lcx.campus.service.impl;

import com.lcx.campus.domain.Role;
import com.lcx.campus.mapper.RoleMapper;
import com.lcx.campus.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> selectRoleByUserId(Long userId) {
        return roleMapper.selectRoleByUserId(userId);
    }

    /**
     * 判断当前用户的角色里是否有超级管理员角色
     */
    @Override
    public boolean isAdmin(Long userId) {
        List<Role> roles = selectRoleByUserId(userId);
        for (Role role : roles) {
            if (role.getRoleKey().equals("admin")) {
                return true;
            }
        }
        return false;
    }
}

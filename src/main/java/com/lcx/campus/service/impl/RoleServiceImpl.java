package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.Role;
import com.lcx.campus.domain.dto.PageQuery;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.mapper.RoleMapper;
import com.lcx.campus.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.SecurityUtils;
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

    @Override
    public Result getCurrentRole() {
        Long userId = SecurityUtils.getUserId();
        return Result.success(selectRoleByUserId(userId));
    }

    @Override
    public Result getPageList(Role role) {
        // 构建分页条件
        Page<Role> page = role.toMpPage();
        // 分页查询
        Page<Role> resultP = lambdaQuery()
                .like(role.getRoleName() != null, Role::getRoleName, role.getRoleName())
                .like(role.getRoleKey() != null, Role::getRoleKey, role.getRoleKey())
                .eq(role.getRoleStatus() != null, Role::getRoleStatus, role.getRoleStatus())
                .page(page);
        PageVo<Role> res = PageVo.of(resultP);

        return Result.success(res);
    }
}

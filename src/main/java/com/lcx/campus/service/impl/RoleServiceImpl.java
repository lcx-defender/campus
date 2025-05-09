package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.Menu;
import com.lcx.campus.domain.Role;
import com.lcx.campus.domain.RoleMenu;
import com.lcx.campus.domain.dto.PageQuery;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.mapper.MenuMapper;
import com.lcx.campus.mapper.RoleMapper;
import com.lcx.campus.mapper.RoleMenuMapper;
import com.lcx.campus.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.SecurityUtils;
import com.lcx.campus.utils.StringUtils;
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
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuMapper menuMapper;

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
        Page<Role> queryPage = role.toMpPage();
        // 分页查询
        Page<Role> resPage = lambdaQuery()
                .like(role.getRoleName() != null, Role::getRoleName, role.getRoleName())
                .like(role.getRoleKey() != null, Role::getRoleKey, role.getRoleKey())
                .eq(role.getRoleStatus() != null, Role::getRoleStatus, role.getRoleStatus())
                .page(queryPage);
        PageVo<Role> res = PageVo.of(resPage);
        return Result.success(res);
    }

    @Override
    public Result getRoleMenu(Long roleId) {
        return Result.success(menuMapper.selectMenuPermsByRoleId(roleId));
    }

    @Override
    public Result addRoleMenu(RoleMenu roleMenu) {
        // 先查询对应的roleID和MenuId是否存在
        Role role = roleMapper.selectById(roleMenu.getRoleId());
        Menu menu = menuMapper.selectById(roleMenu.getMenuId());
        if (StringUtils.isNull(role)) {
            return Result.fail("角色不存在");
        }
        if (StringUtils.isNull(menu)) {
            return Result.fail("菜单不存在");
        }
        int insert = roleMenuMapper.insert(roleMenu);
        if (insert > 0) {
            return Result.success("添加成功");
        } else {
            return Result.fail("添加失败");
        }
    }

    /**
     * 统计所有角色使用菜单的数量
     */
    @Override
    public int countRoleWithMenu(Long menuId) {
        return roleMenuMapper.countRoleWithMenu(menuId);
    }

    @Override
    public Result deleteRoleMenu(RoleMenu roleMenu) {
        return roleMenuMapper.deleteRoleMenu(roleMenu) ? Result.success("删除成功") : Result.fail("删除失败");
    }

    @Override
    public Result deleteRoleAllMenu(Long roleId) {
        return roleMenuMapper.deleteRoleMenuByRoleId(roleId) ? Result.success("删除成功") : Result.fail("删除失败");
    }

    /**
     * 通过用户id获取角色信息
     */
    @Override
    public Result getUserRole(Long userId) {
        List<Role> roles = roleMapper.selectRoleByUserId(userId);
        return Result.success(roles);
    }
}

package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.po.Menu;
import com.lcx.campus.domain.po.Role;
import com.lcx.campus.domain.po.User;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.domain.vo.RoleMenusVo;
import com.lcx.campus.mapper.MenuMapper;
import com.lcx.campus.mapper.RoleMapper;
import com.lcx.campus.mapper.RoleMenuMapper;
import com.lcx.campus.mapper.UserRoleMapper;
import com.lcx.campus.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.SecurityUtils;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
    @Resource
    private UserRoleMapper userRoleMapper;

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

    /**
     * 获取当前登录用户的角色信息
     */
    @Override
    public Result getCurrentRole() {
        Long userId = SecurityUtils.getUserId();
        return Result.success(selectRoleByUserId(userId));
    }

    /**
     * 分页查询角色信息
     */
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
    public Result getMenusByRoleId(Long roleId) {
        // 如果是admin角色，返回所有权限菜单
        Role role = roleMapper.selectById(roleId);
        if(role.getRoleKey().equals("admin")) {
            return Result.success(menuMapper.selectMenuList(new Menu()));
        }
        List<Menu> menus = menuMapper.selectMenusByRoleId(roleId);
        return Result.success(menus);
    }

    /**
     * 通过用户id获取角色信息
     */
    @Override
    public Result getUserRole(Long userId) {
        List<Role> roles = roleMapper.selectRoleByUserId(userId);
        return Result.success(roles);
    }

    /**
     * 给角色分配菜单权限
     */
    @Override
    public Result grantRoleMenus(RoleMenusVo roleMenusVo) {
        // 检查角色是否存在
        Role role = roleMapper.selectById(roleMenusVo.getRoleId());
        if (StringUtils.isNull(role)) {
            return Result.fail("角色不存在");
        }
        // 对菜单Id进行去重
        Long[] menuIds = Arrays.stream(roleMenusVo.getMenuIds())
                .distinct()
                .toArray(Long[]::new);
        // 检查角色是否存在旧的权限菜单
        List<Menu> oldMenus = menuMapper.selectMenusByRoleId(roleMenusVo.getRoleId());
        if (menuIds.length == 0) {
            // 如果没有新的权限菜单，删除旧的权限菜单
            if (oldMenus.size() == 0) {
                return Result.success("角色已经没有权限菜单", null);
            }
            return roleMenuMapper.deleteRoleMenuByRoleId(roleMenusVo.getRoleId()) ?
                    Result.success("角色权限菜单已清空", null) :
                    Result.fail("删除角色原有菜单权限失败");
        }
        // 检查菜单是否都存在
        List<Menu> menus = menuMapper.selectBatchIds(Arrays.asList(menuIds));
        if (menus.size() != menuIds.length) {
            return Result.fail("有菜单不存在");
        }
        // 删除原有的菜单权限
        boolean isSuccess = roleMenuMapper.deleteRoleMenuByRoleId(roleMenusVo.getRoleId());
        if (!isSuccess && oldMenus.size() > 0) {
            return Result.fail("删除角色原有菜单权限失败");
        }
        // 添加角色新的菜单权限
        int rows = roleMenuMapper.insertBatchRoleMenu(roleMenusVo.getRoleId(), menuIds);
        return rows == menuIds.length ?
                Result.success("授权成功", null) :
                Result.fail("授权失败");
    }

    /**
     * 查询角色被授予的用户
     */
    @Override
    public Result getUsersByRoleId(Long roleId) {
        List<User> users = userRoleMapper.selectUserByRoleId(roleId);
        return Result.success(users);
    }

    /**
     * 解绑角色对应的所有用户
     */
    @Override
    public Result unbindRoleUsers(Long roleId) {
        // 检查角色是否存在
        Role role = roleMapper.selectById(roleId);
        if (StringUtils.isNull(role)) {
            return Result.fail("角色不存在");
        }
        // 删除所有跟roleId有关的用户
        boolean isSuccess = userRoleMapper.deleteByRoleId(roleId);
        return isSuccess ?
                Result.success("解绑成功") :
                Result.fail("解绑失败");
    }

    @Override
    public Result deleteRoles(Long[] roleIds) {
        roleIds = Arrays.stream(roleIds)
                .distinct()
                .toArray(Long[]::new); // 先对数组进行去重
        List<User> users = userRoleMapper.selectByRoleIds(roleIds); // 检查当前角色有无被用户使用
        if(users.size() > 0) {
            return Result.fail("当前角色已被用户使用,不允许删除");
        }
        // 检查是否有roleKey为admin的角色
        boolean isAdmin = Arrays.stream(roleIds)
                .map(roleMapper::selectById)
                .anyMatch(role -> role.getRoleKey().equals("admin"));
        if (isAdmin) {
            return Result.fail("无法删除超级管理员角色");
        }
        roleMenuMapper.deleteRoleMenuByRoleIds(roleIds); // 删除角色对应的菜单权限
        boolean isSuccess = removeBatchByIds(Arrays.asList(roleIds)); // 删除角色
        return isSuccess ? Result.success() : Result.fail("角色删除失败");
    }
}
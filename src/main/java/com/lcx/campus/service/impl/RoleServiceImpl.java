package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.*;
import com.lcx.campus.domain.dto.PageQuery;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.domain.vo.RoleMenusVo;
import com.lcx.campus.mapper.MenuMapper;
import com.lcx.campus.mapper.RoleMapper;
import com.lcx.campus.mapper.RoleMenuMapper;
import com.lcx.campus.mapper.UserRoleMapper;
import com.lcx.campus.service.IMenuService;
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
    @Resource
    private IMenuService menuService;

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
     * @return
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


    /**
     * 通过用户id获取角色信息
     */
    @Override
    public Result getUserRole(Long userId) {
        List<Role> roles = roleMapper.selectRoleByUserId(userId);
        return Result.success(roles);
    }

    /**
     * 删除角色信息
     */
    @Override
    public Result deleteRole(Long roleId) {
        // 检查是否还存在绑定的用户,若有则删除失败
        List<UserRole> userRoles = userRoleMapper.selectByRoleId(roleId);
        if (userRoles.size() > 0) {
            return Result.fail("该角色已绑定用户,请先解除绑定");
        }
        // 检查是否角色绑定了菜单，若有直接解除绑定
        List<RoleMenu> roleMenus = roleMenuMapper.selectByRoleId(roleId);
        if (roleMenus.size() > 0) {
            roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        }
        int count = roleMapper.deleteById(roleId);
        if (count > 0) {
            return Result.success("删除成功");
        } else {
            return Result.fail("删除失败");
        }
    }

    /**
     * 查询角色对应的权限菜单树型选择器
     */
    @Override
    public Result getRoleMenuTreeSelect(Long roleId) {
        // 查询所有菜单
        List<Menu> menus = menuMapper.selectAllMenuListByRoleId(roleId);
        // 构建treeSelect
        return Result.success(menuService.buildMenuTreeSelect(menus));
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
        if (menuIds.length == 0) {
            return roleMenuMapper.deleteRoleMenuByRoleId(roleMenusVo.getRoleId()) ?
                    Result.success("删除成功") :
                    Result.fail("删除失败");
        }
        // 检查菜单是否都存在
        List<Menu> menus = menuMapper.selectBatchIds(Arrays.asList(menuIds));
        if (menus.size() != menuIds.length) {
            return Result.fail("有菜单不存在");
        }
        // 删除原有的菜单权限
        List<Long> oldMenuIds = menuMapper.selectMenuIdsByRoleId(roleMenusVo.getRoleId());
        boolean isSuccess = roleMenuMapper.deleteRoleMenuByRoleId(roleMenusVo.getRoleId());
        if(!isSuccess && oldMenuIds.size() > 0) {
            return Result.fail("删除角色原有菜单权限失败");
        }
        // 添加角色新的菜单权限
        int rows = roleMenuMapper.insertBatchRoleMenu(roleMenusVo.getRoleId(), menuIds);
        return rows == menuIds.length ?
                Result.success("授权成功") :
                Result.fail("授权失败");
    }

    /**
     * 查询角色被授予的用户
     */
    @Override
    public Result getRoleUsers(Long roleId) {
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
}

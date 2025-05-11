package com.lcx.campus.service;

import com.lcx.campus.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.RoleMenu;
import com.lcx.campus.domain.dto.PageQuery;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.RoleMenusVo;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IRoleService extends IService<Role> {

    List<Role> selectRoleByUserId(Long userId);

    boolean isAdmin(Long userId);

    Result getCurrentRole();

    Result getPageList(Role role);

    Result getMenusByRoleId(Long roleId);


    /**
     * 通过用户id获取角色信息
     */
    Result getUserRole(Long userId);

    /**
     * 给角色分配菜单权限
     */
    Result grantRoleMenus(RoleMenusVo roleMenusVo);

    /**
     * 查询角色被授予的用户
     */
    Result getUsersByRoleId(Long roleId);

    /**
     * 解绑角色对应的所有用户
     */
    Result unbindRoleUsers(Long roleId);

    Result deleteRoles(Long[] roleIds);
}

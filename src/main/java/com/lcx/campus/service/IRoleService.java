package com.lcx.campus.service;

import com.lcx.campus.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.RoleMenu;
import com.lcx.campus.domain.dto.PageQuery;
import com.lcx.campus.domain.dto.Result;

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

    Result getRoleMenu(Long roleId);

    Result addRoleMenu(RoleMenu roleMenu);

    int countRoleWithMenu(Long menuId);

    /**
     * 解除指定角色与指定权菜单的关联
     * @param roleMenu
     * @return
     */
    Result deleteRoleMenu(RoleMenu roleMenu);

    Result deleteRoleAllMenu(Long roleId);

    /**
     * 通过用户id获取角色信息
     */
    Result getUserRole(Long userId);
}

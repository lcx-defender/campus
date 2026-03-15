package com.lcx.campus.mapper;

import com.lcx.campus.domain.po.RoleMenu;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 Mapper 接口
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface RoleMenuMapper {

    int countRoleWithMenu(Long menuId);

    boolean deleteRoleMenu(RoleMenu roleMenu);

    boolean deleteRoleMenuByRoleId(Long roleId);

    boolean deleteRoleMenuByRoleIds(Long[] roleId);

    int insert(RoleMenu roleMenu);

    List<RoleMenu> selectByRoleId(Long roleId);

    int insertBatchRoleMenu(Long roleId, Long[] menuIds);

    boolean deleteRoleMenuByMenuId(Long menuId);
}

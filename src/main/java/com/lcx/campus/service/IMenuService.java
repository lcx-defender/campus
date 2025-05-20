package com.lcx.campus.service;

import com.lcx.campus.domain.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.domain.vo.RouterVo;
import com.lcx.campus.domain.vo.TreeSelect;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IMenuService extends IService<Menu> {

    Set<Menu> selectMenuByUserId(Long userId);

    /**
     * 获取登录用户的前端路由
     */
    Result getRouters(Long userId);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenuList(Long userId);

    /**
     * 根据用户查询系统菜单列表
     */
    List<Menu> selectMenuList(Menu menu, Long userId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenuTreeByUserId(Long userId);

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    List<Menu> buildMenuTree(List<Menu> menus);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    List<TreeSelect> buildMenuTreeSelect(List<Menu> menus);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterVo> buildMenus(List<Menu> menus);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean hasChildByMenuId(Long menuId);

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkMenuExistRole(Long menuId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    boolean checkMenuNameUnique(Menu menu);

    /**
     * 删除与角色的绑定关系
     */
    boolean deleteRoleMenuByMenuId(Long menuId);

    /**
     * 根据条件查询相关菜单
     */
    List<Menu> selectMenuList(Menu menu);

    /**
     * 根据menuId删除菜单
     */
    Result removeMenuById(Long menuId);
}

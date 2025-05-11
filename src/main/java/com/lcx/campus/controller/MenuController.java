package com.lcx.campus.controller;


import com.lcx.campus.domain.Menu;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.enums.MenuStatus;
import com.lcx.campus.service.IMenuService;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("hasAnyAuthority('system:menu:list')")
    @GetMapping("/getMenuTree")
    public Result getMenuTree() {
        List<Menu> menus = menuService.selectMenuList(SecurityUtils.getUserId());
        return Result.success(menuService.buildMenuTree(menus));
    }

    /**
     * 获取菜单下拉树列表
     */
    @PreAuthorize("hasAnyAuthority('system:menu:list')")
    @GetMapping("/treeSelect")
    public Result treeSelect() {
        Menu menu = new Menu();
        // TODO 设置菜单状态之后建立treeSelect就失败了，还未处理
//        menu.setMenuStatus(MenuStatus.NORMAL.getValue());
        List<Menu> menus = menuService.selectMenuList(menu); // 获取所有状态正常的菜单
        return Result.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @PreAuthorize("hasAnyAuthority('system:menu:list')")
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public Result roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        List<Menu> menus = menuService.selectMenuList(SecurityUtils.getUserId());
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return Result.success(ajax);
    }

    /**
     * 新增菜单
     */
    @PreAuthorize("hasAnyAuthority('system:menu:add')")
    @PostMapping
    public Result add(@Validated @RequestBody Menu menu) {
        if (menuService.checkMenuNameUnique(menu)) {
            return Result.fail("新增菜单'" + menu.getMenuName() + "'失败,菜单名称已存在");
        }
        return Result.success(menuService.save(menu));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("hasAnyAuthority('system:menu:remove')")
    @DeleteMapping("/{menuId}")
    public Result remove(@PathVariable Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return Result.fail("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            // 删除与角色的绑定关系
            boolean isSuccess= menuService.deleteRoleMenuByMenuId(menuId);
        }
        return Result.success(menuService.removeById(menuId));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("hasAnyAuthority('system:menu:edit')")
    @PutMapping
    public Result edit(@Validated @RequestBody Menu menu) {
        if (menuService.checkMenuNameUnique(menu)) {
            return Result.fail("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        return Result.success(menuService.updateById(menu));
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    @GetMapping("/{menuId}")
    public Result getInfo(@PathVariable Long menuId) {
        return Result.success(menuService.getById(menuId));
    }
}

package com.lcx.campus.controller;


import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.Menu;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.enums.MenuStatus;
import com.lcx.campus.service.IMenuService;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    @PostMapping("/getMenuTree")
    @Log(title = "获取菜单列表", businessType = BusinessType.QUERY)
    public Result getMenuTree(@RequestBody Menu menu) {
        List<Menu> menus = menuService.selectMenuList(menu);
        return Result.success(menuService.buildMenuTree(menus));
    }
    /**
     * 获取菜单下拉树列表
     */
    @PreAuthorize("hasAnyAuthority('system:menu:list')")
    @GetMapping("/treeSelect")
    @Log(title = "获取菜单下拉树列表", businessType = BusinessType.QUERY)
    public Result treeSelect() {
        Menu menu = new Menu();
        menu.setMenuStatus(MenuStatus.NORMAL.getValue());
        List<Menu> menus = menuService.selectMenuList(menu); // 获取所有状态正常的菜单
        return Result.success(menuService.buildMenuTreeSelect(menus));
    }
    /**
     * 新增菜单
     */
    @PreAuthorize("hasAnyAuthority('system:menu:add')")
    @PostMapping("/addMenu")
    public Result addMenu(@Validated @RequestBody Menu menu) {
        if (menuService.checkMenuNameUnique(menu)) {
            menu.setCreateTime(LocalDateTime.now());
            return menuService.save(menu) ? Result.success() : Result.fail("新增菜单'" + menu.getMenuName() + "'失败");
        }
        return Result.fail("新增菜单'" + menu.getMenuName() + "'失败,菜单名称已存在");
    }
    /**
     * 根据menuId删除菜单
     */
    @PreAuthorize("hasAnyAuthority('system:menu:remove')")
    @DeleteMapping("/delete/{menuId}")
    public Result removeById(@PathVariable Long menuId) {
        return menuService.removeMenuById(menuId);
    }
    /**
     * 修改菜单
     */
    @PreAuthorize("hasAnyAuthority('system:menu:edit')")
    @PutMapping("/editMenu")
    public Result edit(@Validated @RequestBody Menu menu) {
        if (menuService.checkMenuNameUnique(menu)) {
            return Result.fail("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        return menuService.updateById(menu) ? Result.success() : Result.fail("修改菜单'" + menu.getMenuName() + "'失败");
    }
    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    @GetMapping("/queryMenu/{menuId}")
    public Result getInfo(@PathVariable Long menuId) {
        return Result.success(menuService.getById(menuId));
    }
}
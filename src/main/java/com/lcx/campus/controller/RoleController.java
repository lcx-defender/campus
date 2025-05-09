package com.lcx.campus.controller;


import com.lcx.campus.domain.Role;
import com.lcx.campus.domain.RoleMenu;
import com.lcx.campus.domain.dto.PageQuery;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.IRoleService;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * * 获取当前登录用户的角色信息
     */
    @GetMapping("/getCurrentRole")
    public Result getCurrentRole() {
        return roleService.getCurrentRole();
    }

    /**
     * 获取所有角色信息
     */
    @PreAuthorize("hasAnyAuthority('system:role:list')")
    @GetMapping("/getAllRole")
    public Result getAllRole() {
        return Result.success(roleService.list());
    }

    /**
     * 分页查询角色信息
     */
    @PreAuthorize("hasAnyAuthority('system:role:list')")
    @PostMapping("/getPageList")
    public Result getPageList(@RequestBody Role role) {
        return roleService.getPageList(role);
    }

    /**
     * 通过id获取角色详细信息
     *
     * @param id 角色id
     * @return 角色信息
     */
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    @GetMapping("/getRole/{id}")
    public Result getRoleById(@PathVariable Long id) {
        return Result.success(roleService.getById(id));
    }



    /**
     * 查询角色对应的权限
     */
    @GetMapping("/getRoleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Long roleId) {
        return roleService.getRoleMenu(roleId);
    }

    /**
     * 添加新的角色信息
     */
    @PreAuthorize("hasAnyAuthority('system:role:add')")
    @PostMapping("/addRole")
    public Result addRole(@Validated @RequestBody Role role) {
        role.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        role.setCreateTime(LocalDateTime.now());
        return Result.success(roleService.save(role));
    }

    /**
     * 给角色添加权限菜单
     * 参数1:roleId
     * 参数2:menuId
     */
    @PostMapping("/addRoleMenu")
    public Result addRoleMenu(@RequestBody RoleMenu roleMenu) {
        return roleService.addRoleMenu(roleMenu);
    }

    /**
     * 修改角色信息
     */
    @PreAuthorize("hasAnyAuthority('system:role:edit')")
    @PutMapping("/updateRole")
    public Result updateRole(@RequestBody Role role) {
        role.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        role.setUpdateTime(LocalDateTime.now());
        return Result.success(roleService.updateById(role));
    }

    /**
     * 删除角色信息
     */
    @PreAuthorize("hasAnyAuthority('system:role:remove')")
    @DeleteMapping("/deleteRole/{id}")
    public Result deleteRole(@PathVariable Long id) {
        return Result.success(roleService.removeById(id));
    }

    /**
     * 删除角色和菜单关联
     */
    @PreAuthorize("hasAnyAuthority('system:role:remove')")
    @DeleteMapping("/deleteRoleMenu")
    public Result deleteRoleMenu(@RequestBody RoleMenu roleMenu) {
        return roleService.deleteRoleMenu(roleMenu);
    }

    /**
     * 解除指定角色的所有权限菜单
     */
    @PreAuthorize("hasAnyAuthority('system:role:remove')")
    @DeleteMapping("/deleteRoleAllMenu/{roleId}")
    public Result deleteRoleAllMenu(@PathVariable Long roleId) {
        return roleService.deleteRoleAllMenu(roleId);
    }

}

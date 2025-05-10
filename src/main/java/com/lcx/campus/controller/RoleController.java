package com.lcx.campus.controller;


import com.lcx.campus.domain.Role;
import com.lcx.campus.domain.RoleMenu;
import com.lcx.campus.domain.dto.PageQuery;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.RoleMenusVo;
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
     * 删除角色信息
     */
    @PreAuthorize("hasAnyAuthority('system:role:remove')")
    @DeleteMapping("/deleteRole/{roleId}")
    public Result deleteRole(@PathVariable Long roleId) {
        return roleService.deleteRole(roleId);
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
     * 通过 roleId 获取角色详细信息
     */
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    @GetMapping("/getRole/{roleId}")
    public Result getRoleById(@PathVariable Long roleId) {
        return Result.success(roleService.getById(roleId));
    }

    /**
     * 查询角色对应的权限字符
     */
    @GetMapping("/getRoleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Long roleId) {
        return roleService.getRoleMenu(roleId);
    }

    /**
     * 查询角色对应的权限菜单树型选择器
     */
    @GetMapping("/getRoleMenuTreeSelect/{roleId}")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public Result getRoleMenuTreeSelect(@PathVariable Long roleId) {
        return roleService.getRoleMenuTreeSelect(roleId);
    }

    /**
     * 查询角色被授予的用户
     */
    @GetMapping("/getRoleUsers/{roleId}")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public Result getRoleUsers(@PathVariable Long roleId) {
        return roleService.getRoleUsers(roleId);
    }

    /**
     * 给角色分配菜单权限
     */
    @PreAuthorize("hasAnyAuthority('system:role:grant')")
    @PutMapping("/grantRoleMenus")
    public Result grantRoleMenus(@Validated @RequestBody RoleMenusVo roleMenusVo) {
        return roleService.grantRoleMenus(roleMenusVo);
    }

    /**
     * 解绑角色对应的所有用户
     */
    @PreAuthorize("hasAnyAuthority('system:role:unbind')")
    @DeleteMapping("/unbindRoleUsers/{roleId}")
    public Result unbindRoleUsers(@PathVariable Long roleId) {
        return roleService.unbindRoleUsers(roleId);
    }
}

package com.lcx.campus.controller;


import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.Teacher;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.LoginBody;
import com.lcx.campus.domain.dto.PasswordBody;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.UserRolesVo;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.enums.UserStatus;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.service.IRoleService;
import com.lcx.campus.service.IUserService;
import com.lcx.campus.utils.SecurityUtils;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    @Resource
    private IRoleService roleService;

    /**
     * 分页查询所有自己部门之下的用户信息
     * 通过用户昵称、用户类型、邮箱、手机号、用户状态去多条件查询
     */
    @PostMapping("/pageList")
    @PreAuthorize("hasAnyAuthority('system:user:list')")
    public Result pageUserList(@RequestBody User user) {
        return userService.pageUserList(user);
    }

    /**
     * 新建系统用户, userTye 为 SYSTEM
     */
    @Log(title = "新建系统用户", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAnyAuthority('system:user:add')")
    @PostMapping("/addUser")
    public Result addUserOfAdmin(@Validated(User.AddUserGroup.class) @RequestBody User user) {
        user.setUserType(UserType.SYSTEM.getCode());
        return userService.addUserOfAdmin(user);
    }

    /**
     * 注销用户(逻辑)
     */
    @Log(title = "删除用户", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAnyAuthority('system:user:remove')")
    @DeleteMapping("/deleteUsers/{userIds}")
    public Result deleteUser(@PathVariable Long[] userIds) {
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            // 判断是不是管理员账户，管理员账户无法删除
            if(userId != null && !roleService.isAdmin(userId)) {
                User user = new User();
                user.setUserId(userId);
                user.setUserStatus(UserStatus.DELETED.getCode());
                users.add(user);
            }
        }
        if(StringUtils.isEmpty(users)) {
            return Result.fail("没有可删除的用户");
        }
        return userService.updateBatchById(users) ? Result.success() : Result.fail("用户删除失败");
    }

    /**
     * 管理员修改他人信息
     */
    @Log(title = "修改用户信息", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('system:user:edit')")
    @PutMapping("/updateUser")
    public Result updateUser(@Validated(User.UpdateUserGroup.class) @RequestBody User user) {
        if(roleService.isAdmin(user.getUserId())) {
            return Result.fail("管理员账户仅可自行修改");
        }
        return userService.updateUser(user);
    }

    /**
     * 用户修改个人信息，只允许个人修改 nickname、email、phone、sex 这四个字段，根据uderId更新
     */
    @Log(title = "修改个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updateSelfInfo")
    public Result updateSelfInfo(@RequestBody User user) {
        return userService.updateSelfInfo(user);
    }

    /**
     * 获取指定用户信息
     */
    @GetMapping("/getUser/{userId}")
    @PreAuthorize("hasAnyAuthority('system:user:query')")
    public Result getUser(@PathVariable Long userId) {
        User user = userService.getById(userId);
        if (StringUtils.isNull(user)) {
            return Result.fail("用户不存在");
        }
        user.setPassword(null); // 隐藏密码
        return Result.success(user);
    }

    /**
     * 通过用户id获取角色信息
     */
    @PreAuthorize("hasAnyAuthority('system:user:query')")
    @GetMapping("/getUserRole/{userId}")
    public Result getUserRole(@PathVariable Long userId) {
        return roleService.getUserRole(userId);
    }

    /**
     * 获取当前登录用户个人信息
     */
    @GetMapping("/getSelfInfo")
    public Result getSelfInfo() {
        return userService.getSelfInfo();
    }

    /**
     * 管理员修改他人密码
     */
    @Log(title = "管理员修改他人密码", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('system:user:reset')")
    @PutMapping("/resetPassword")
    public Result resetPassword(
            @Validated(PasswordBody.AdminResetGroup.class) @RequestBody PasswordBody passwordBody) {
        if(roleService.isAdmin(passwordBody.getUserId())) {
            return Result.fail("无法修改管理员账户");
        }
        return userService.resetPassword(passwordBody);
    }

    /**
     * 修改自己密码
     */
    @Log(title = "修改个人密码", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePassword")
    public Result updatePassword(
            @Validated(PasswordBody.UserUpdateGroup.class) @RequestBody PasswordBody passwordBody, HttpServletRequest request) {
        return userService.updatePassword(passwordBody, request);
    }

    /**
     * 封禁用户
     */
    @Log(title = "封禁用户", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('system:user:ban')")
    @PutMapping("/banUser/{userIds}")
    public Result banUser(@PathVariable Long[] userIds) {
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            // 判断是不是管理员账户，管理员账户无法删除
            if(userId != null && !roleService.isAdmin(userId)) {
                User user = new User();
                user.setUserId(userId);
                user.setUserStatus(UserStatus.DISABLE.getCode());
                users.add(user);
            }
        }
        if(StringUtils.isEmpty(users)) {
            return Result.fail("没有可封禁的用户");
        }
        return userService.updateBatchById(users) ? Result.success() : Result.fail("用户封禁失败");
    }

    /**
     * 恢复用户状态为正常
     */
    @Log(title = "恢复用户", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('system:user:recover')")
    @PutMapping("/recoverUser/{userIds}")
    public Result recoverUser(@PathVariable Long[] userIds) {
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            if(userId != null) {
                User user = new User();
                user.setUserId(userId);
                user.setUserStatus(UserStatus.OK.getCode());
                users.add(user);
            }
        }
        if(StringUtils.isEmpty(users)) {
            return Result.fail("没有可恢复的用户");
        }
        return userService.updateBatchById(users) ? Result.success() : Result.fail("用户恢复失败");
    }

    /**
     * 管理员修改用户角色信息
     */
    @Log(title = "修改用户角色", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('system:user:grant')")
    @PutMapping("/updateUserRoles")
    public Result updateUserRoles(@RequestBody UserRolesVo userRolesVo) {
        if(roleService.isAdmin(userRolesVo.getUserId()) && !roleService.isAdmin(SecurityUtils.getUserId())) {
            return Result.fail("管理员账户仅可自行修改");
        }
        return userService.updateUserRoles(userRolesVo);
    }
}

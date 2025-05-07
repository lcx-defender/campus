package com.lcx.campus.controller;


import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.Teacher;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.LoginBody;
import com.lcx.campus.domain.dto.PasswordBody;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.enums.UserStatus;
import com.lcx.campus.service.IRoleService;
import com.lcx.campus.service.IUserService;
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
     * 获取当前登录用户个人信息
     */
    @GetMapping("/getSelfInfo")
    public Result getSelfInfo() {
        return userService.getSelfInfo();
    }

    /**
     * 获取指定用户信息
     */
    @GetMapping("/getUser/{userId}")
    @PreAuthorize("hasAnyAuthority('system:user:list')")
    public Result getUser(@PathVariable Long userId) {
        User user = userService.getById(userId);
        user.setPassword(null);
        if (StringUtils.isNull(user)) {
            return Result.fail("用户不存在");
        }
        return Result.success(user);
    }


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
     * 修改自己密码
     */
    @Log(title = "修改个人密码", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePassword")
    public Result updatePassword(
            @Validated(PasswordBody.UserUpdateGroup.class) @RequestBody PasswordBody passwordBody, HttpServletRequest request) {
        return userService.updatePassword(passwordBody, request);
    }

    /**
     * 管理员修改他人密码
     */
    @Log(title = "管理员修改他人密码", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('system:user:edit')")
    @PutMapping("/resetPassword")
    public Result resetPassword(
            @Validated(PasswordBody.AdminResetGroup.class) @RequestBody PasswordBody passwordBody) {
        if(roleService.isAdmin(passwordBody.getUserId())) {
            return Result.fail("无法修改管理员账户");
        }
        return userService.resetPassword(passwordBody);
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
     * 管理员修改他人信息
     */
    @Log(title = "修改用户信息", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('system:user:edit')")
    @PutMapping("/updateUser")
    public Result updateUser(@Validated(User.UpdateUserGroup.class) @RequestBody User user) {
        if(roleService.isAdmin(user.getUserId())) {
            return Result.fail("管理员账户无法修改");
        }
        return userService.updateUser(user);
    }

    /**
     * 新建系统用户(开发测试等人员使用,不对外开放)
     */
    @Log(title = "新建系统用户", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAnyAuthority('system:user:add')")
    @PostMapping("/addUser")
    public Result addUserOfAdmin(@Validated(User.AddUserGroup.class) @RequestBody User user) {
        return userService.addUserOfAdmin(user);
    }

    /**
     * 删除用户(逻辑)
     */
    @Log(title = "删除用户", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAnyAuthority('system:user:remove')")
    @DeleteMapping("/deleteUsers")
    public Result deleteUser(@RequestBody List<Long> userIds) {
        if(StringUtils.isEmpty(userIds)) {
            return Result.fail("用户ID不能为空");
        }
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
        return userService.updateBatchById(users) ? Result.success() : Result.fail("用户删除失败");
    }

    /**
     * 封禁用户
     */
    @Log(title = "封禁用户", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('system:user:edit')")
    @PutMapping("/banUser/{userId}")
    public Result banUser(@PathVariable Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUserStatus(UserStatus.DISABLE.getCode());
        return userService.updateUser(user);
    }

}

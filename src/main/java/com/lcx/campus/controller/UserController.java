package com.lcx.campus.controller;


import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.Teacher;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.LoginBody;
import com.lcx.campus.domain.dto.PasswordBody;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取当前登录用户个人信息
     */
    @GetMapping("/getSelfInfo")
    public Result getSelfInfo() {
        return userService.getSelfInfo();
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
    @PreAuthorize("hasAnyAuthority('system:user:resetPwd')")
    @PutMapping("/resetPassword")
    public Result resetPassword(
            @Validated(PasswordBody.AdminResetGroup.class) @RequestBody PasswordBody passwordBody) {
        return userService.resetPassword(passwordBody);
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

}

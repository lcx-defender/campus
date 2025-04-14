package com.lcx.campus.controller;


import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.service.IStudentService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private IStudentService studentService;

    /**
     * 添加学生用户
     */
    /**
     * 新建学生类型用户
     */
    @Log(title = "新建学生类型用户", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAnyAuthority('system:student:add')")
    @PostMapping("/addStudent")
    public Result addStudent(@Validated(User.AddUserGroup.class) @RequestBody User user, @RequestBody Student student) {
        return studentService.addStudent(user, student);
    }
}

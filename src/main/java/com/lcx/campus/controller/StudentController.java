package com.lcx.campus.controller;


import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.service.IStudentService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * 分页查询学生信息
     */
    @GetMapping("/pageList")
    @PreAuthorize("hasAnyAuthority('system:student:list')")
    public Result pageList(@RequestBody Student student) {
        return studentService.pageList(student);
    }

    /**
     * 新建学生类型用户
     */
    @Log(title = "新建学生类型用户", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAnyAuthority('system:student:add')")
    @PostMapping("/addStudent")
    public Result addStudent(@Validated(User.AddUserGroup.class) @RequestBody User user, @RequestBody Student student) {
        user.setUserType(UserType.STUDENT.getCode());
        return studentService.addStudent(user, student);
    }

    /**
     * 修改学生信息
     */
    @Log(title = "修改学生信息", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('system:student:edit')")
    @PutMapping("/editStudent")
    public Result editStudent(@RequestBody Student student) {
        return studentService.editStudent(student);
    }
}

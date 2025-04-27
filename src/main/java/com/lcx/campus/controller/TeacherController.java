package com.lcx.campus.controller;


import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.Teacher;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.PageQuery;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.service.ITeacherService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 教师表 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private ITeacherService teacherService;

    /**
     * 分页查询教师列表
     */
    @Log(title = "查询教师列表", businessType = BusinessType.QUERY)
    @PreAuthorize("hasAnyAuthority('system:teacher:list')")
    @GetMapping("/pageList")
    public Result listTeacher(@RequestBody Teacher teacher) {
        return teacherService.pageListTeacher(teacher);
    }

    /**
     * 新建教师类型用户
     */
    @Log(title = "新建教师类型用户", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAnyAuthority('system:teacher:add')")
    @PostMapping("/addTeacher")
    public Result addUserOfTeacher(@Validated(User.AddUserGroup.class) @RequestBody User user,
                                   @Validated(Teacher.addTeacher.class) @RequestBody Teacher teacher) {
        return teacherService.addTeacher(user, teacher);
    }

    /**
     * 修改教师部分信息
     */
    @Log(title = "修改教师信息", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('system:teacher:edit')")
    @PutMapping("/editTeacher")
    public Result editTeacher(@Validated(Teacher.updateInfo.class) @RequestBody Teacher teacher) {
        return teacherService.editTeacher(teacher);
    }
}

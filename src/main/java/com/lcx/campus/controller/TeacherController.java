package com.lcx.campus.controller;


import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.po.Teacher;
import com.lcx.campus.domain.po.User;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.domain.dto.TeacherUser;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.service.ITeacherService;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.BeanUtils;
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
    @PreAuthorize("hasAnyAuthority('campus:teacher:list')")
    @PostMapping("/pageList")
    public Result listTeacher(@RequestBody Teacher teacher) {
        return teacherService.pageListTeacher(teacher);
    }
    /**
     * 分页查询教师列表
     */
    @Log(title = "查询教师用户列表", businessType = BusinessType.QUERY)
    @PreAuthorize("hasAnyAuthority('campus:teacher:list')")
    @PostMapping("/pageTeacherUser")
    public Result pageTeacherUser(@RequestBody TeacherUser teacherUser) {
        return teacherService.pageListTeacherUser(teacherUser);
    }
    /**
     * 新建教师类型用户
     */
    @Log(title = "新建教师类型用户", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAnyAuthority('campus:teacher:add')")
    @PostMapping("/addTeacher")
    public Result addUserOfTeacher(@Validated @RequestBody TeacherUser teacherUser) {
        User user = new User();
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherUser, user);
        BeanUtils.copyProperties(teacherUser, teacher);
        user.setUserType(UserType.TEACHER.getCode());
        return teacherService.addTeacher(user, teacher);
    }
    /**
     * 修改教师用户信息
     */
    @Log(title = "修改教师信息", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('campus:teacher:edit')")
    @PutMapping("/editTeacher")
    public Result editTeacher(@RequestBody TeacherUser teacherUser) {
        return teacherService.editTeacherUser(teacherUser);
    }
    /**
     * 学生视角查询教师信息
     */
    @Log(title = "学生检索查询教师信息", businessType = BusinessType.QUERY)
    @GetMapping("/getTeacherInfo")
    public Result getTeacherList(@PathParam("teacherName") String teacherName) {
        Teacher teacher = new Teacher();
        teacher.setTeacherName(teacherName);
        return Result.success(teacherService.getTeacherList(teacher));
    }
}
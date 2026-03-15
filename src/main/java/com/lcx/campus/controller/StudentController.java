package com.lcx.campus.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.po.Student;
import com.lcx.campus.domain.po.User;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.domain.dto.StudentUser;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.listener.ExcelListener;
import com.lcx.campus.service.IStudentService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 分页查询学生用户信息
     */
    @PostMapping("/pageStudentUser")
    @PreAuthorize("hasAnyAuthority('campus:student:list')")
    @Log(title = "分页查询学生用户信息", businessType = BusinessType.QUERY)
    public Result pageStudentUser(@RequestBody StudentUser studentUser) {
        return studentService.pageStudentUser(studentUser);
    }
    /**
     * 新增学生类型用户
     */
    @Log(title = "新建学生类型用户", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAnyAuthority('campus:student:add')")
    @PostMapping("/addStudent")
    public Result addStudent(@Validated @RequestBody StudentUser studentUser) {
        studentUser.setUserType(UserType.STUDENT.getCode());
        User user = new User();
        Student student = new Student();
        BeanUtils.copyProperties(studentUser, user);
        BeanUtils.copyProperties(studentUser, student);
        return studentService.addStudent(user, student);
    }
    /**
     * 通过Excel批量新增学生类型用户
     */
    @Log(title = "通过Excel批量新增学生类型用户", businessType = BusinessType.IMPORT)
    @PreAuthorize("hasAnyAuthority('campus:student:add')")
    @PostMapping("/batchAddStudent")
    public Result batchAddStudent(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), StudentUser.class,
                    new ExcelListener<StudentUser>(
                            list -> studentService.batchAddStudent((List<StudentUser>) list)
                    )).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success("批量添加学生成功", null);
    }
    /**
     * 批量导出学生用户信息
     */
    @Log(title = "批量导出学生用户信息", businessType = BusinessType.EXPORT)
    @PreAuthorize("hasAnyAuthority('campus:student:export')")
    @PostMapping("/exportStudent")
    public void exportStudent(HttpServletResponse response, @RequestBody StudentUser studentUser) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("学生用户信息", "UTF-8").replaceAll("\\+", "%20");;
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<StudentUser> studentUsers = studentService.selectStudentUserList(studentUser);
            System.out.println(studentUsers.size() + "-----------------");
            EasyExcel.write(response.getOutputStream(), StudentUser.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("学生用户信息").doWrite(studentUsers);
        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }
    /**
     * 修改学生信息
     */
    @Log(title = "修改学生信息", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('campus:student:edit')")
    @PutMapping("/editStudent")
    public Result editStudent(@RequestBody StudentUser studentUser) {
        return studentService.editStudent(studentUser);
    }
}
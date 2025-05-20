package com.lcx.campus.service;

import com.lcx.campus.domain.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.domain.dto.StudentUser;

import java.util.List;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IStudentService extends IService<Student> {
    /**
     * 新增学生类型用户
     */
    Result addStudent(User user, Student student);
    /**
     * 通过Excel批量新增学生类型用户
     */
    Result batchAddStudent(List<StudentUser> studentUsers);

    Result editStudent(StudentUser studentUser);

    /**
     * 条件查询所有学生用户信息
     */
    List<StudentUser> selectStudentUserList(StudentUser studentUser);
    /**
     * 分页条件查询学生用户信息
     */
    Result pageStudentUser(StudentUser studentUser);
}

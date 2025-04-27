package com.lcx.campus.service;

import com.lcx.campus.domain.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.Result;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IStudentService extends IService<Student> {

    Result addStudent(User user, Student student);

    Result pageList(Student student);

    Result editStudent(Student student);
}

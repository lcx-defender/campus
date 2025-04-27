package com.lcx.campus.service;

import com.lcx.campus.domain.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.Result;

/**
 * <p>
 * 教师表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface ITeacherService extends IService<Teacher> {

    Result addTeacher(User user, Teacher teacher);

    Result pageListTeacher(Teacher teacher);

    Result editTeacher(Teacher teacher);
}

package com.lcx.campus.service.impl;

import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.mapper.StudentMapper;
import com.lcx.campus.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Resource
    private IUserService userService;
    @Resource
    private StudentMapper studentMapper;

    @Override
    public Result addStudent(User user, Student student) {
        user.setUserType(UserType.STUDENT.ordinal());
        Long userId = userService.creatUserIfNotExist(user);
        if (userId == null) {
            return Result.fail("用户添加失败");
        }
        // 3. 插入学生信息
        student.setUserId(userId);
        int insert = studentMapper.insert(student);
        if (insert <= 0) {
            return Result.fail("添加学生信息失败");
        }
        return Result.success("添加学生成功", userId);
    }
}

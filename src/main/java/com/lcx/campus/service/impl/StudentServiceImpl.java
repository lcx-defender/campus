package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.domain.Dept;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.dto.StudentUser;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.enums.DeptLevel;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.mapper.StudentMapper;
import com.lcx.campus.mapper.TeacherMapper;
import com.lcx.campus.mapper.UserMapper;
import com.lcx.campus.service.IDeptService;
import com.lcx.campus.service.IStudentService;
import com.lcx.campus.service.IUserService;
import com.lcx.campus.utils.SecurityUtils;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    @Resource
    private UserMapper userMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private IDeptService deptService;
    /**
     * 分页查询学生用户信息
     */
    @Override
    public Result pageStudentUser(StudentUser studentUser) {
        // 根据用户类型进行不同的查询
        User currentUser = userMapper.selectById(SecurityUtils.getUserId());
        if (currentUser.getUserType().equals(UserType.SYSTEM.getCode())) {
            // 系统用户查询所有部门
            studentUser.setUniversityId(null);
            studentUser.setInstituteId(null);
            studentUser.setMajorId(null);
            studentUser.setClassId(null);
        } else if(currentUser.getUserType().equals(UserType.TEACHER.getCode())) {
            // 教师用户查询自己所在的部门
            Dept dept = teacherMapper.selectDeptByUserId(currentUser.getUserId());
            if (dept.getLevel().equals(DeptLevel.UNIVERSITY.getLevel())) {
                studentUser.setUniversityId(dept.getDeptId());
                studentUser.setInstituteId(null);
                studentUser.setMajorId(null);
                studentUser.setClassId(null);
            } else if (dept.getLevel().equals(DeptLevel.INSTITUTE.getLevel())) {
                studentUser.setInstituteId(dept.getDeptId());
            } else if (dept.getLevel().equals(DeptLevel.MAJOR.getLevel())) {
                studentUser.setMajorId(dept.getDeptId());
            } else if (dept.getLevel().equals(DeptLevel.CLAZZ.getLevel())) {
                studentUser.setClassId(dept.getDeptId());
            }
        }
        Page<StudentUser> queryPage  = studentUser.toMpPage();
//        List<StudentUser> studentUsers = studentMapper.selectStudentUserPage(queryPage, studentUser);
//        PageVo<StudentUser> res = PageVo.of(queryPage.setRecords(studentUsers));
        Page<StudentUser> resPage = studentMapper.selectStudentUserPage(queryPage, studentUser);
        PageVo<StudentUser> res = PageVo.of(resPage);
        return Result.success("查询成功", res);
    }
    @Override
    public Result addStudent(User user, Student student) {
        user.setUserType(UserType.STUDENT.getCode());
        Long userId = userService.creatUserIfNotExist(user);
        if (userId == null) {
            return Result.fail("用户添加失败");
        }
        student.setUserId(userId);
        // 校验学生的单位信息是否合规
        boolean validate = deptService.validateDept(student);
        if(!validate) {
            return Result.fail("学生的单位信息不合规");
        }
        int insert = studentMapper.insert(student);
        if (insert <= 0) {
            return Result.fail("添加学生信息失败");
        }
        return Result.success("添加学生成功", userId);
    }
    @Override
    public Result batchAddStudent(List<StudentUser> studentUsers) {
        // 插入用户
        List<Student> students = new ArrayList<>();
        for (StudentUser studentUser : studentUsers) {
            // 将studentUser里跟user有关字段全部封装成user对象
            User user = new User();
            BeanUtils.copyProperties(studentUser, user);
            user.setUserType(UserType.STUDENT.getCode());
            Long userId = userService.creatUserIfNotExist(user);
            if (userId == null) {
                return Result.fail("用户添加失败");
            }
            Student student = new Student();
            BeanUtils.copyProperties(studentUser, student);
            student.setUserId(userId);
            students.add(student);
        }
        return saveBatch(students)? Result.success("添加学生成功", null) : Result.fail("添加学生失败");
    }
    @Override
    public Result editStudent(StudentUser studentUser) {
        User user = new User();
        Student student = new Student();
        BeanUtils.copyProperties(studentUser, user);
        BeanUtils.copyProperties(studentUser, student);
        userService.updateUser(user);
        return updateById(student) ? Result.success("修改成功", null) : Result.fail("修改失败");
    }
    @Override
    public List<StudentUser> selectStudentUserList(StudentUser studentUser) {
        return studentMapper.selectStudentUserList(studentUser);
    }
}
package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.domain.Dept;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.vo.Result;
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
        Page<StudentUser> queryPage = studentUser.toMpPage();
        Dept queryDept = null;
        if (StringUtils.isNotNull(studentUser.getDeptId())) {
            queryDept = deptService.getById(studentUser.getDeptId());
            if (queryDept == null) {
                return Result.fail("查询的部门不存在");
            }
        }
        // 根据用户类型进行不同的查询
        User currentUser = SecurityUtils.getLoginUser().getUser();
        if (currentUser.getUserType().equals(UserType.SYSTEM.getCode())) {
            if (StringUtils.isNull(queryDept)) {
                // 系统用户查询所有部门
                studentUser.setUniversityId(null);
                studentUser.setInstituteId(null);
                studentUser.setMajorId(null);
                studentUser.setClassId(null);
            } else {
                // 系统用户查询指定部门
                studentUser = setQuery(queryDept, studentUser, queryDept);// 设置查询条件
            }
        } else if (currentUser.getUserType().equals(UserType.TEACHER.getCode())) {
            // 教师用户查询自己所在的部门
            Dept dept = teacherMapper.selectDeptByUserId(currentUser.getUserId());
            if (dept.getLevel().equals(DeptLevel.UNIVERSITY.getLevel())) {
                if (queryDept.getLevel() != null) {
                    studentUser = setQuery(queryDept, studentUser, dept);// 设置查询条件
                    if (StringUtils.isNull(studentUser)) {
                        return Result.fail("查询的部门不在教师权限范围内");
                    }
                } else {
                    // 未设置查询条件默认按照教师所在部门去查询
                    studentUser.setUniversityId(dept.getDeptId());
                    studentUser.setInstituteId(null);
                    studentUser.setMajorId(null);
                    studentUser.setClassId(null);
                }
            } else if (dept.getLevel().equals(DeptLevel.INSTITUTE.getLevel())) {
                if (queryDept.getLevel() != null) {
                    studentUser = setQuery(queryDept, studentUser, dept);// 设置查询条件
                    if (StringUtils.isNull(studentUser)) {
                        return Result.fail("查询的部门不在教师权限范围内");
                    }
                } else {
                    // 未设置查询条件默认按照教师所在部门去查询
                    studentUser.setInstituteId(dept.getDeptId());
                }
            } else if (dept.getLevel().equals(DeptLevel.MAJOR.getLevel())) {
                if (queryDept.getLevel() != null) {
                    studentUser = setQuery(queryDept, studentUser, dept);// 设置查询条件
                    if (StringUtils.isNull(studentUser)) {
                        return Result.fail("查询的部门不在教师权限范围内");
                    }
                } else {
                    // 未设置查询条件默认按照教师所在部门去查询
                    studentUser.setMajorId(dept.getDeptId());
                }
            } else if (dept.getLevel().equals(DeptLevel.CLAZZ.getLevel())) {
                if (queryDept.getLevel() != null) {
                    studentUser = setQuery(queryDept, studentUser, dept);// 设置查询条件
                    if (StringUtils.isNull(studentUser)) {
                        return Result.fail("查询的部门不在教师权限范围内");
                    }
                } else {
                    // 未设置查询条件默认按照教师所在部门去查询
                    studentUser.setClassId(dept.getDeptId());
                }
            }
        }
        Page<StudentUser> resPage = studentMapper.selectStudentUserPage(queryPage, studentUser);
        PageVo<StudentUser> res = PageVo.of(resPage);
        return Result.success("查询成功", res);
    }

    /**
     * 设置StudentUser查询条件
     *
     */
    private StudentUser setQuery(Dept queryDept, StudentUser studentUser, Dept dept) {
        if ((queryDept.getLevel() >= dept.getLevel())
                && deptService.isParentDept(dept.getDeptId(), queryDept.getDeptId())) { // 查询的部门是老师权限下的子部门
            // 根据所在部门层次设置查询条件
            if (queryDept.getLevel().equals(DeptLevel.UNIVERSITY.getLevel())) {
                studentUser.setUniversityId(queryDept.getDeptId());
                studentUser.setInstituteId(null);
                studentUser.setMajorId(null);
                studentUser.setClassId(null);
            } else if (queryDept.getLevel().equals(DeptLevel.INSTITUTE.getLevel())) {
                studentUser.setUniversityId(null);
                studentUser.setInstituteId(queryDept.getDeptId());
                studentUser.setMajorId(null);
                studentUser.setClassId(null);
            } else if (queryDept.getLevel().equals(DeptLevel.MAJOR.getLevel())) {
                studentUser.setUniversityId(null);
                studentUser.setInstituteId(null);
                studentUser.setMajorId(queryDept.getDeptId());
                studentUser.setClassId(null);
            } else if (queryDept.getLevel().equals(DeptLevel.CLAZZ.getLevel())) {
                studentUser.setUniversityId(null);
                studentUser.setInstituteId(null);
                studentUser.setMajorId(null);
                studentUser.setClassId(queryDept.getDeptId());
            }
            return studentUser; // 返回设置好的查询条件
        } else {
            return null; // 查询的部门不是老师权限下的子部门
        }
    }

    @Override
    public Result addStudent(User user, Student student) {
        user.setUserType(UserType.STUDENT.getCode());
        Long userId = userService.creatUserIfNotExist(user);
        if (userId == null) {
            return Result.fail("学生用户添加失败，请检查用户信息是否完整或已存在");
        }
        student.setUserId(userId);
        // 校验学生的单位信息是否合规
        boolean validate = deptService.validateDept(student);
        if (!validate) {
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
                return Result.fail("学生用户添加失败，请检查用户信息是否完整或已存在");
            }
            Student student = new Student();
            BeanUtils.copyProperties(studentUser, student);
            student.setUserId(userId);
            students.add(student);
        }
        return saveBatch(students) ? Result.success("添加学生成功", null) : Result.fail("添加学生失败");
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
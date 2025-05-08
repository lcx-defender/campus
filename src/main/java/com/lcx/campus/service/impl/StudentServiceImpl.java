package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.Dept;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.enums.DeptLevel;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.mapper.StudentMapper;
import com.lcx.campus.mapper.TeacherMapper;
import com.lcx.campus.mapper.UserMapper;
import com.lcx.campus.service.IDeptService;
import com.lcx.campus.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.service.IUserService;
import com.lcx.campus.utils.SecurityUtils;
import com.lcx.campus.utils.StringUtils;
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
    @Resource
    private UserMapper userMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private IDeptService deptService;

    @Override
    public Result addStudent(User user, Student student) {
        user.setUserType(UserType.STUDENT.getCode());
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

    @Override
    public Result pageList(Student student) {
        // 解析分页查询条件
        Page<Student> queryPage = student.toMpPage();
        // 获取当前用户的userID
        Long userId = SecurityUtils.getUserId();
        User user = userMapper.selectById(userId);
        if (user.getUserType().equals(UserType.STUDENT.getCode())) {
            // 如果是学生用户，查询当前用户的班级
            Student currentStudent = studentMapper.selectById(userId);
            if (student.getDeptId() != null && currentStudent.getClassId() != null && student.getDeptId().equals(currentStudent.getClassId())) {
                // 如果传入的班级ID和当前用户的班级ID相同，则查询该班级的学生
                Page<Student> resPage = lambdaQuery()
                        .eq(Student::getClassId, currentStudent.getClassId())
                        .like(student.getStudentName() != null, Student::getStudentName, student.getStudentName())
                        .page(queryPage);
                return Result.success("查询成功", resPage);
            } else {
                return Result.fail("学生用户没有权限查看其他班级的学生信息");
            }
        }
        // 查询当前教师所属dept
        Dept dept = teacherMapper.selectDeptByUserId(userId);
        if (StringUtils.isNull(dept)) {
            return Result.fail("用户没有可查看的学生信息");
        }
        Page<Student> page;
        if (student.getDeptId() == null) {
            // 如果没有传入班级ID，则查询该教师所在部门的所有学生
            Integer currentLevel = dept.getLevel();
            if (currentLevel.equals(DeptLevel.UNIVERSITY.getLevel())) {
                // 如果是大学级别，则查询大学所有学生
                page = lambdaQuery()
                        .eq(Student::getUniversityId, dept.getDeptId())
                        .like(student.getStudentName() != null, Student::getStudentName, student.getStudentName())
                        .page(queryPage);
            } else if (currentLevel.equals(DeptLevel.INSTITUTE.getLevel())) {
                // 如果是学院级别，则查询该学院的所有学生
                page = lambdaQuery()
                        .eq(Student::getInstituteId, dept.getDeptId())
                        .like(student.getStudentName() != null, Student::getStudentName, student.getStudentName())
                        .page(queryPage);
            } else if (currentLevel.equals(DeptLevel.MAJOR.getLevel())) {
                // 如果是专业级别，则查询该专业的所有学生
                page = lambdaQuery()
                        .eq(Student::getMajorId, dept.getDeptId())
                        .like(student.getStudentName() != null, Student::getStudentName, student.getStudentName())
                        .page(queryPage);
            } else if (currentLevel.equals(DeptLevel.CLAZZ.getLevel())) {
                // 查询班级级别的学生
                page = lambdaQuery()
                        .eq(Student::getClassId, dept.getDeptId())
                        .like(student.getStudentName() != null, Student::getStudentName, student.getStudentName())
                        .page(queryPage);
            } else {
                return Result.fail("用户没有可查看的学生信息");
            }
        } else if (deptService.isParentDept(dept.getDeptId(), student.getDeptId())) {
            // 如果传入了查询deptId，并且是当前用户的子部门，则查询该部门的所有学生
            Dept stuDept = (Dept) deptService.getDeptInfo(student.getDeptId()).getData();
            if (stuDept.getLevel().equals(DeptLevel.UNIVERSITY.getLevel())) {
                // 如果是大学级别，则查询大学所有学生
                page = lambdaQuery()
                        .eq(Student::getUniversityId, stuDept.getDeptId())
                        .like(student.getStudentName() != null, Student::getStudentName, student.getStudentName())
                        .page(queryPage);
            } else if (stuDept.getLevel().equals(DeptLevel.INSTITUTE.getLevel())) {
                // 如果是学院级别，则查询该学院的所有学生
                page = lambdaQuery()
                        .eq(Student::getInstituteId, stuDept.getDeptId())
                        .like(student.getStudentName() != null, Student::getStudentName, student.getStudentName())
                        .page(queryPage);
            } else if (stuDept.getLevel().equals(DeptLevel.MAJOR.getLevel())) {
                // 如果是专业级别，则查询该专业的所有学生
                page = lambdaQuery()
                        .eq(Student::getMajorId, stuDept.getDeptId())
                        .like(student.getStudentName() != null, Student::getStudentName, student.getStudentName())
                        .page(queryPage);
            } else if (stuDept.getLevel().equals(DeptLevel.CLAZZ.getLevel())) {
                // 查询班级级别的学生
                page = lambdaQuery()
                        .eq(Student::getClassId, stuDept.getDeptId())
                        .like(student.getStudentName() != null, Student::getStudentName, student.getStudentName())
                        .page(queryPage);
            } else {
                return Result.fail("用户没有可查看的学生信息");
            }
        } else {
            return Result.fail("用户没有可查看的学生信息");
        }
        PageVo<Student> res = PageVo.of(page);
        return Result.success("查询成功", res);
    }

    @Override
    public Result editStudent(Student student) {
        return updateById(student) ? Result.success("修改成功") : Result.fail("修改失败");
    }
}

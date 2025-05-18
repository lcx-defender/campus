package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.Dept;
import com.lcx.campus.domain.DormitoryInfo;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.enums.DeptLevel;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.mapper.*;
import com.lcx.campus.service.IDormitoryInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.SecurityUtils;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 学生宿舍信息表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class DormitoryInfoServiceImpl extends ServiceImpl<DormitoryInfoMapper, DormitoryInfo> implements IDormitoryInfoService {

    @Resource
    private DormitoryInfoMapper dormitoryInfoMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private StudentMapper studentMapper;

    /**
     * 获取宿舍信息列表
     */
    @Override
    public Result getDormitoryInfoPage(DormitoryInfo dormitoryInfo) {
        Page<DormitoryInfo> queryPage = dormitoryInfo.toMpPage();
        Student student = new Student();
        if (StringUtils.isNotEmpty(dormitoryInfo.getStudentName())) {
            student.setStudentName(dormitoryInfo.getStudentName());
        }
        if (StringUtils.isNotNull(dormitoryInfo.getStudentId())) {
            student.setStudentId(dormitoryInfo.getStudentId());
        }
        List<Student> students = new ArrayList<>();
        User user = SecurityUtils.getLoginUser().getUser();
        if (user.getUserType().equals(UserType.TEACHER.getCode())) {
            Dept dept = teacherMapper.selectDeptByUserId(SecurityUtils.getUserId());
            if (dept.getLevel().equals(DeptLevel.UNIVERSITY.getLevel())) {
                student.setUniversityId(dept.getDeptId());
                students = studentMapper.selectStudentList(student);
            } else if (dept.getLevel().equals(DeptLevel.INSTITUTE.getLevel())) {
                student.setInstituteId(dept.getDeptId());
                students = studentMapper.selectStudentList(student);
            } else if (dept.getLevel().equals(DeptLevel.MAJOR.getLevel())) {
                student.setMajorId(dept.getDeptId());
                students = studentMapper.selectStudentList(student);
            } else {
                student.setClassId(dept.getDeptId());
                students = studentMapper.selectStudentList(student);
            }

        } else if(user.getUserType().equals(UserType.STUDENT.getCode())) {
            return Result.fail("查询方式不正确");
        } else {
            students = studentMapper.selectStudentList(student);
        }
        // 获取这些学生studentId
        List<String> studentIds = students.stream().map(Student::getStudentId).toList();
        // 分页查询学生的宿舍信息
        Page<DormitoryInfo> resPage = lambdaQuery()
                .eq(dormitoryInfo.getDormitoryId() != null, DormitoryInfo::getDormitoryId, dormitoryInfo.getDormitoryId())
                .eq(dormitoryInfo.getRoomId() != null, DormitoryInfo::getRoomId, dormitoryInfo.getRoomId())
                .eq(dormitoryInfo.getBedId() != null, DormitoryInfo::getBedId, dormitoryInfo.getBedId())
                .in(DormitoryInfo::getStudentId, studentIds)
                .page(queryPage);
        return Result.success(resPage);
    }

    /**
     * 单独增加宿舍信息
     */
    @Override
    public Result addDormitoryInfo(DormitoryInfo dormitoryInfo) {
        // 查询是否当前床位是否已经有人居住
        DormitoryInfo info = dormitoryInfoMapper.selectDormitoryInfoByLocation(dormitoryInfo);
        if (StringUtils.isNotNull(info)) {
            return Result.fail("当前床位已经有人居住");
        }
        // 查询学生是否已经拥有床位
        DormitoryInfo existingInfo = dormitoryInfoMapper.selectDormitoryInfoByStudentId(dormitoryInfo.getStudentId());
        if (StringUtils.isNotNull(existingInfo)) {
            return Result.fail("当前学生已经拥有床位");
        }
        // 查询当前学生是否存在
        Student student = new Student();
        student.setStudentId(dormitoryInfo.getStudentId());
        student = studentMapper.selectStudent(student);
        if (StringUtils.isNull(student)) {
            return Result.fail("当前学生不存在");
        }
        return save(dormitoryInfo) ? Result.success("添加成功", null) : Result.fail("添加失败");
    }

    @Override
    public void addBatchDormitoryInfo(List<DormitoryInfo> list) {
        List<DormitoryInfo> conflictList = new ArrayList<>();
        for (DormitoryInfo dormitoryInfo : list) {
            // 查询数据库中是否存在冲突
            DormitoryInfo existingInfo = dormitoryInfoMapper.selectDormitoryInfoByLocation(dormitoryInfo);
            if (existingInfo != null) {
                conflictList.add(dormitoryInfo);
            }
            // 查询学生是否已经拥有床位
            DormitoryInfo existingStudentInfo = dormitoryInfoMapper.selectDormitoryInfoByStudentId(dormitoryInfo.getStudentId());
            if (existingStudentInfo != null) {
                conflictList.add(dormitoryInfo);
            }
            // 查询当前学生是否存在
            Student student = new Student();
            student.setStudentId(dormitoryInfo.getStudentId());
            student = studentMapper.selectStudent(student);
            if (StringUtils.isNull(student)) {
                throw new RuntimeException("当前学生不存在: " + dormitoryInfo.getStudentId());
            }
        }
        // 如果存在冲突，抛出异常或返回冲突信息
        if (!conflictList.isEmpty()) {
            throw new RuntimeException("以下宿舍信息存在冲突: " + conflictList);
        }
        // 批量保存数据
        saveBatch(list);
    }

    /**
     * 获取当前用户的宿舍信息
     */
    @Override
    public Result getCurrentUserDormitoryInfo() {
        User user = SecurityUtils.getLoginUser().getUser();
        if (!user.getUserType().equals(UserType.STUDENT.getCode())) {
            return Result.fail("当前用户不是学生");
        }
        DormitoryInfo dormitoryInfo = dormitoryInfoMapper.selectDormitoryInfoByUserId(user.getUserId());
        return Result.success(dormitoryInfo);
    }

    /**
     * 修改宿舍信息
     */
    @Override
    public Result update(DormitoryInfo dormitoryInfo) {
        // 查询是否当前床位是否已经有人居住
        DormitoryInfo info = dormitoryInfoMapper.selectDormitoryInfoByLocation(dormitoryInfo);
        if (StringUtils.isNotNull(info) && !info.getId().equals(dormitoryInfo.getId())) {
            return Result.fail("当前床位已经有人居住");
        }
        DormitoryInfo dormitoryInfo1 = dormitoryInfoMapper.selectDormitoryInfoByStudentId(dormitoryInfo.getStudentId());
        if (StringUtils.isNotNull(dormitoryInfo1) && !dormitoryInfo1.getId().equals(dormitoryInfo.getId())) {
            return Result.fail("当前学生已经拥有床位");
        }
        // 查询当前学生是否存在
        Student student = new Student();
        student.setStudentId(dormitoryInfo.getStudentId());
        student = studentMapper.selectStudent(student);
        if (StringUtils.isNull(student)) {
            return Result.fail("当前学生不存在");
        }
        return updateById(dormitoryInfo) ? Result.success("修改成功", null) : Result.fail("修改失败");
    }

    @Override
    public List<DormitoryInfo> selectDormitoryInfoList(DormitoryInfo dormitoryInfo) {
        Student student = new Student();
        if (StringUtils.isNotEmpty(dormitoryInfo.getStudentName())) {
            student.setStudentName(dormitoryInfo.getStudentName());
        }
        if (StringUtils.isNotNull(dormitoryInfo.getStudentId())) {
            student.setStudentId(dormitoryInfo.getStudentId());
        }
        List<Student> students = new ArrayList<>();
        User user = SecurityUtils.getLoginUser().getUser();
        if (user.getUserType().equals(UserType.TEACHER.getCode())) {
            Dept dept = teacherMapper.selectDeptByUserId(SecurityUtils.getUserId());
            if (dept.getLevel().equals(DeptLevel.UNIVERSITY.getLevel())) {
                student.setUniversityId(dept.getDeptId());
                students = studentMapper.selectStudentList(student);
            } else if (dept.getLevel().equals(DeptLevel.INSTITUTE.getLevel())) {
                student.setInstituteId(dept.getDeptId());
                students = studentMapper.selectStudentList(student);
            } else if (dept.getLevel().equals(DeptLevel.MAJOR.getLevel())) {
                student.setMajorId(dept.getDeptId());
                students = studentMapper.selectStudentList(student);
            } else {
                student.setClassId(dept.getDeptId());
                students = studentMapper.selectStudentList(student);
            }
        } else if(user.getUserType().equals(UserType.STUDENT.getCode())) {
            return List.of();
        } else {
            students = studentMapper.selectStudentList(student);
        }
        // 获取这些学生studentId
        List<String> studentIds = students.stream().map(Student::getStudentId).toList();
        // 条件查询学生的宿舍信息
        return dormitoryInfoMapper.selectDormitoryInfoList(dormitoryInfo, studentIds);
    }
}
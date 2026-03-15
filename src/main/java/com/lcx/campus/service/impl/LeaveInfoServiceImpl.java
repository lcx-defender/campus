package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.domain.po.LeaveInfo;
import com.lcx.campus.domain.po.Student;
import com.lcx.campus.domain.po.Teacher;
import com.lcx.campus.domain.po.User;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.enums.ApprovalStatus;
import com.lcx.campus.enums.LeaveType;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.mapper.StudentMapper;
import com.lcx.campus.mapper.TeacherMapper;
import com.lcx.campus.service.LeaveInfoService;
import com.lcx.campus.mapper.LeaveInfoMapper;
import com.lcx.campus.utils.SecurityUtils;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 15403
 * @description 针对表【leave_info】的数据库操作Service实现
 * @createDate 2025-05-20 10:41:23
 */
@Service
public class LeaveInfoServiceImpl extends ServiceImpl<LeaveInfoMapper, LeaveInfo>
        implements LeaveInfoService {
    @Resource
    private LeaveInfoMapper leaveInfoMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private TeacherMapper teacherMapper;

    /**
     * 分页查询请假信息
     */
    @Override
    public PageVo<LeaveInfo> getLeaveInfoPage(LeaveInfo leaveInfo) {
        // 获取当前用户类型
        User user = SecurityUtils.getLoginUser().getUser();
        if (user.getUserType().equals(UserType.STUDENT.getCode())) {
            Student student = studentMapper.selectById(user.getUserId());
            leaveInfo.setApplicant(student.getStudentId());

        } else if (user.getUserType().equals(UserType.TEACHER.getCode())) {
            // 教师查询需要自己审批的请假信息
            Teacher teacher = teacherMapper.selectById(user.getUserId());
            leaveInfo.setApprover(teacher.getTeacherId());
        }
        Page<LeaveInfo> queryPage = leaveInfo.toMpPage();
        Page<LeaveInfo> resPage = lambdaQuery()
                .eq(leaveInfo.getLeaveType() != null, LeaveInfo::getLeaveType, leaveInfo.getLeaveType())
                .eq(leaveInfo.getApplicant() != null, LeaveInfo::getApplicant, leaveInfo.getApplicant())
                .eq(leaveInfo.getApprover() != null, LeaveInfo::getApprover, leaveInfo.getApprover())
                .eq(leaveInfo.getApprovalStatus() != null, LeaveInfo::getApprovalStatus, leaveInfo.getApprovalStatus())
                .orderByDesc(LeaveInfo::getCreateTime) // 按照创建时间降序排列
                .page(queryPage);
        return PageVo.of(resPage);
    }
    @Override
    public List<LeaveInfo> getLeaveInfoList(LeaveInfo leaveInfo) {
        // 获取当前用户类型
        User user = SecurityUtils.getLoginUser().getUser();
        if (user.getUserType().equals(UserType.STUDENT.getCode())) {
            Student student = studentMapper.selectById(user.getUserId());
            leaveInfo.setApplicant(student.getStudentId());

        } else if (user.getUserType().equals(UserType.TEACHER.getCode())) {
            // 教师查询需要自己审批的请假信息
            Teacher teacher = teacherMapper.selectById(user.getUserId());
            leaveInfo.setApprover(teacher.getTeacherId());
        }
        return lambdaQuery()
                .eq(leaveInfo.getLeaveType() != null, LeaveInfo::getLeaveType, leaveInfo.getLeaveType())
                .eq(leaveInfo.getApplicant() != null, LeaveInfo::getApplicant, leaveInfo.getApplicant())
                .eq(leaveInfo.getApprover() != null, LeaveInfo::getApprover, leaveInfo.getApprover())
                .eq(leaveInfo.getApprovalStatus() != null, LeaveInfo::getApprovalStatus, leaveInfo.getApprovalStatus())
                .orderByDesc(LeaveInfo::getCreateTime)
                .list();
    }

    /**
     * 学生提交请假信息
     */
    @Override
    public boolean addLeaveInfo(LeaveInfo leaveInfo) {
        leaveInfo.setCreateTime(LocalDateTime.now());
        // 获取当前用户类型
        User user = SecurityUtils.getLoginUser().getUser();
        if (user.getUserType().equals(UserType.STUDENT.getCode())) {
            Student student = studentMapper.selectById(user.getUserId());
            leaveInfo.setApplicant(student.getStudentId());
        } else {
            return false;
        }
        if (LeaveType.getByCode(leaveInfo.getLeaveType()) == null
        || ApprovalStatus.getByCode(leaveInfo.getApprovalStatus()) == null) {
            return false;
        }
        return false;
    }

    /**
     * 修改自己的请假信息
     */
    @Override
    public boolean updateLeaveInfo(LeaveInfo leaveInfo) {
        // 获取当前用户类型
        User user = SecurityUtils.getLoginUser().getUser();
        Student student = studentMapper.selectById(user.getUserId());
        LeaveInfo exist = leaveInfoMapper.selectById(leaveInfo.getLeaveInfoId());
        if (StringUtils.isNull(exist) || !user.getUserType().equals(UserType.STUDENT.getCode()) || StringUtils.isNull(student)
                || !student.getStudentId().equals(exist.getApplicant())
        || !ApprovalStatus.PENDING.getCode().equals(exist.getApprovalStatus())) {
            // 请假信息不存在 或 不是学生 或 不是申请人 或 不是待审批状态
            return false;
        }
        leaveInfo.setApplicant(student.getStudentId());
        leaveInfo.setCreateTime(LocalDateTime.now()); // 修改之后重新计算提交时间
        return updateById(leaveInfo);
    }
    /**
     * 教师审批请假信息
     */
    @Override
    public boolean check(LeaveInfo leaveInfo) {
        // 获取当前用户类型
        User user = SecurityUtils.getLoginUser().getUser();
        Teacher teacher = teacherMapper.selectById(user.getUserId());
        LeaveInfo exist = leaveInfoMapper.selectById(leaveInfo.getLeaveInfoId());
        if (StringUtils.isNull(exist) || !user.getUserType().equals(UserType.TEACHER.getCode()) || StringUtils.isNull(teacher)
                || !teacher.getTeacherId().equals(exist.getApprover())) {
            // 请假信息不存在 或 不是教师 或 不是审批人
            return false;
        }
        leaveInfo.setApprovalTime(LocalDateTime.now());
        return updateById(leaveInfo);
    }
}
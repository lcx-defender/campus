package com.lcx.campus.controller;

import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.LeaveInfo;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.enums.ApprovalStatus;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.enums.LeaveType;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.mapper.StudentMapper;
import com.lcx.campus.service.LeaveInfoService;
import com.lcx.campus.utils.SecurityUtils;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/leaveInfo")
public class LeaveInfoController {
    @Resource
    private LeaveInfoService leaveInfoService;
    @Resource
    private StudentMapper studentMapper;

    /**
     * 教师/管理员分页查询请假信息，学生只能查询自己的请假信息
     */
    @PostMapping("/getLeaveInfoPage")
    @PreAuthorize("hasAnyAuthority('campus:leaveInfo:list')")
    @Log(title = "分页查询请假信息", businessType = BusinessType.UPDATE)
    public Result getLeaveInfoPage(@RequestBody LeaveInfo leaveInfo) {
        return Result.success(leaveInfoService.getLeaveInfoPage(leaveInfo));
    }

    @GetMapping("/getLeaveInfoList")
    @Log(title = "学生查询个人请假信息列表", businessType = BusinessType.UPDATE)
    public Result getSelfLeaveInfoList() {
        return Result.success(leaveInfoService.getLeaveInfoList(new LeaveInfo()));
    }

    /**
     * 学生提交请假信息
     */
    @PostMapping("/addLeaveInfo")
    @Log(title = "提交请假信息", businessType = BusinessType.INSERT)
    public Result addLeaveInfo(@Validated @RequestBody LeaveInfo leaveInfo) {
        if (leaveInfoService.addLeaveInfo(leaveInfo)) {
            return Result.success();
        }
        return Result.fail("请假信息提交失败");
    }

    /**
     * 修改自己的请假信息
     */
    @PutMapping("/updateLeaveInfo")
    @Log(title = "修改请假信息", businessType = BusinessType.UPDATE)
    public Result updateLeaveInfo(@Validated(value = LeaveInfo.update.class) @RequestBody LeaveInfo leaveInfo) {
        if (leaveInfoService.updateLeaveInfo(leaveInfo)) {
            return Result.success();
        }
        return Result.fail("请假信息修改失败");
    }

    /**
     * 学生撤销请假申请
     */
    @PutMapping("/cancelLeaveInfo/{leaveInfoId}")
    @Log(title = "学生撤销请假申请", businessType = BusinessType.UPDATE)
    public Result cancelSelfLeaveInfo(@PathVariable Long leaveInfoId) {
        LeaveInfo existLeaveInfo = leaveInfoService.getById(leaveInfoId);
        if (StringUtils.isNull(existLeaveInfo)) {
            return Result.fail("请假信息不存在");
        }
        // 获取当前用户类型
        User user = SecurityUtils.getLoginUser().getUser();
        if (user.getUserType().equals(UserType.STUDENT.getCode())) {
            Student student = studentMapper.selectById(user.getUserId());
            if(!student.getStudentId().equals(existLeaveInfo.getApplicant())) {
                return Result.fail("非学生用户本人，无法撤销");
            }
        } else {
            return Result.fail("非学生用户，无法撤销");
        }
        existLeaveInfo.setApprovalStatus(ApprovalStatus.CANCEL.getCode());
        return leaveInfoService.updateById(existLeaveInfo) ? Result.success() : Result.fail("更新失败");
    }

    /**
     * 教师审批请假信息
     */
    @PutMapping("/approveLeaveInfo/{leaveInfoId}")
    @Log(title = "审批请假信息", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('campus:leaveInfo:check')")
    public Result approveLeaveInfo(@PathVariable Long leaveInfoId) {
        LeaveInfo leaveInfo = new LeaveInfo();
        leaveInfo.setLeaveInfoId(leaveInfoId);
        leaveInfo.setApprovalStatus(ApprovalStatus.APPROVED.getCode());
        if (leaveInfoService.check(leaveInfo)) {
            return Result.success();
        }
        return Result.fail("请假信息审批失败");
    }

    /**
     * 教师拒绝请假信息
     */
    @PutMapping("/rejectLeaveInfo/{leaveInfoId}")
    @Log(title = "拒绝请假信息", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAnyAuthority('campus:leaveInfo:check')")
    public Result rejectLeaveInfo(@PathVariable Long leaveInfoId) {
        LeaveInfo leaveInfo = new LeaveInfo();
        leaveInfo.setLeaveInfoId(leaveInfoId);
        leaveInfo.setApprovalStatus(ApprovalStatus.REJECTED.getCode());
        if (leaveInfoService.check(leaveInfo)) {
            return Result.success();
        }
        return Result.fail("请假信息拒绝失败");
    }

    /**
     * 学生查询
     */
    @GetMapping("/getLeaveInfo/{leaveInfoId}")
    @Log(title = "查询请假信息", businessType = BusinessType.QUERY)
    public Result getLeaveInfo(@PathVariable Long leaveInfoId) {
        LeaveInfo leaveInfo = leaveInfoService.getById(leaveInfoId);
        if (leaveInfo == null) {
            return Result.fail("请假信息不存在");
        }
        return Result.success(leaveInfo);
    }
}

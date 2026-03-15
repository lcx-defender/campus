package com.lcx.campus.service;

import com.lcx.campus.domain.po.LeaveInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.vo.PageVo;

import java.util.List;

/**
 * @author 刘传星
 * @since 2025-05-20
 */
public interface LeaveInfoService extends IService<LeaveInfo> {
    /**
     * 分页查询请假信息
     */
    PageVo<LeaveInfo> getLeaveInfoPage(LeaveInfo leaveInfo);
    /**
     * 学生提交请假信息
     */
    boolean addLeaveInfo(LeaveInfo leaveInfo);
    /**
     * 修改自己的请假信息
     */
    boolean updateLeaveInfo(LeaveInfo leaveInfo);

    /**
     * 教师审批请假信息
     */
    boolean check(LeaveInfo leaveInfo);

    /**
     * 获取请假信息列表，按时间降序排列
     */
    List<LeaveInfo> getLeaveInfoList(LeaveInfo leaveInfo);
}

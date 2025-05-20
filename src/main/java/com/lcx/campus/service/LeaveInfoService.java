package com.lcx.campus.service;

import com.lcx.campus.domain.LeaveInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.vo.PageVo;

/**
* @author 15403
* @description 针对表【leave_info】的数据库操作Service
* @createDate 2025-05-20 10:41:23
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
}

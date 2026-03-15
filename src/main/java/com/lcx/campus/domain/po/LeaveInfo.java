package com.lcx.campus.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.lcx.campus.domain.query.PageQuery;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 请假信息表
 *
 * @author 刘传星
 * @since 2025-05-20
 */
@TableName(value ="leave_info")
@Data
public class LeaveInfo extends PageQuery implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    @NotNull(message = "请假信息id不能为空", groups = {update.class})
    private Long leaveInfoId;
    /**
     * "1" 住宿请假 "2" 课堂请假
     */
    @NotNull(message = "请假类型不能为空")
    private String leaveType;

    /**
     * 
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 
     */
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    /**
     * 
     */
    @NotNull(message = "紧急联系人不能为空")
    private String emergencyContact;

    /**
     * 
     */
    @NotNull(message = "去向不能为空")
    private String destination;

    /**
     * 
     */
    @NotNull(message = "请假事由不能为空")
    private String reason;

    /**
     * 
     */
    private List<String> attachmentUrls;

    /**
     * 审批人的 userId
     */
    private String approver;

    /**
     * 审批状态 "0" 待审批 "1" 通过 ”2“ 驳回
     */
    private String approvalStatus;

    /**
     * 审批时间
     */
    private LocalDateTime approvalTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 申请人userId
     */
    private String applicant;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public interface update {}
}
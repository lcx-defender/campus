package com.lcx.campus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.lcx.campus.domain.dto.PageQuery;
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
     * 0 校内 1 校外
     */
    @NotNull(message = "是否校外不能为空")
    private String isOffCampus;

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
    @NotNull(message = "请假附件不能为空")
    private String attachmentUrl1;

    /**
     * 
     */
    private String attachmentUrl2;

    /**
     * 
     */
    private String attachmentUrl3;

    /**
     * 
     */
    private String attachmentUrl4;

    /**
     * 审批人
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
     * 申请人
     */
    private String applicant;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public interface update {}

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LeaveInfo other = (LeaveInfo) that;
        return (this.getLeaveInfoId() == null ? other.getLeaveInfoId() == null : this.getLeaveInfoId().equals(other.getLeaveInfoId()))
            && (this.getLeaveType() == null ? other.getLeaveType() == null : this.getLeaveType().equals(other.getLeaveType()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getIsOffCampus() == null ? other.getIsOffCampus() == null : this.getIsOffCampus().equals(other.getIsOffCampus()))
            && (this.getEmergencyContact() == null ? other.getEmergencyContact() == null : this.getEmergencyContact().equals(other.getEmergencyContact()))
            && (this.getDestination() == null ? other.getDestination() == null : this.getDestination().equals(other.getDestination()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()))
            && (this.getAttachmentUrl1() == null ? other.getAttachmentUrl1() == null : this.getAttachmentUrl1().equals(other.getAttachmentUrl1()))
            && (this.getAttachmentUrl2() == null ? other.getAttachmentUrl2() == null : this.getAttachmentUrl2().equals(other.getAttachmentUrl2()))
            && (this.getAttachmentUrl3() == null ? other.getAttachmentUrl3() == null : this.getAttachmentUrl3().equals(other.getAttachmentUrl3()))
            && (this.getAttachmentUrl4() == null ? other.getAttachmentUrl4() == null : this.getAttachmentUrl4().equals(other.getAttachmentUrl4()))
            && (this.getApprover() == null ? other.getApprover() == null : this.getApprover().equals(other.getApprover()))
            && (this.getApprovalStatus() == null ? other.getApprovalStatus() == null : this.getApprovalStatus().equals(other.getApprovalStatus()))
            && (this.getApprovalTime() == null ? other.getApprovalTime() == null : this.getApprovalTime().equals(other.getApprovalTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getApplicant() == null ? other.getApplicant() == null : this.getApplicant().equals(other.getApplicant()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getLeaveInfoId() == null) ? 0 : getLeaveInfoId().hashCode());
        result = prime * result + ((getLeaveType() == null) ? 0 : getLeaveType().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getIsOffCampus() == null) ? 0 : getIsOffCampus().hashCode());
        result = prime * result + ((getEmergencyContact() == null) ? 0 : getEmergencyContact().hashCode());
        result = prime * result + ((getDestination() == null) ? 0 : getDestination().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        result = prime * result + ((getAttachmentUrl1() == null) ? 0 : getAttachmentUrl1().hashCode());
        result = prime * result + ((getAttachmentUrl2() == null) ? 0 : getAttachmentUrl2().hashCode());
        result = prime * result + ((getAttachmentUrl3() == null) ? 0 : getAttachmentUrl3().hashCode());
        result = prime * result + ((getAttachmentUrl4() == null) ? 0 : getAttachmentUrl4().hashCode());
        result = prime * result + ((getApprover() == null) ? 0 : getApprover().hashCode());
        result = prime * result + ((getApprovalStatus() == null) ? 0 : getApprovalStatus().hashCode());
        result = prime * result + ((getApprovalTime() == null) ? 0 : getApprovalTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getApplicant() == null) ? 0 : getApplicant().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", leaveInfoId=").append(leaveInfoId);
        sb.append(", leaveType=").append(leaveType);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", isOffCampus=").append(isOffCampus);
        sb.append(", emergencyContact=").append(emergencyContact);
        sb.append(", destination=").append(destination);
        sb.append(", reason=").append(reason);
        sb.append(", attachmentUrl1=").append(attachmentUrl1);
        sb.append(", attachmentUrl2=").append(attachmentUrl2);
        sb.append(", attachmentUrl3=").append(attachmentUrl3);
        sb.append(", attachmentUrl4=").append(attachmentUrl4);
        sb.append(", approver=").append(approver);
        sb.append(", approvalStatus=").append(approvalStatus);
        sb.append(", approvalTime=").append(approvalTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", applicant=").append(applicant);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
package com.lcx.campus.enums;

/**
 * 审批状态 "0" 待审批 "1" 通过 ”2“ 驳回 "3" 撤销
 *
 * @author 刘传星
 * @since 2025-05-20
 */
public enum ApprovalStatus {
    PENDING("0", "待审批"),
    APPROVED("1", "通过"),
    REJECTED("2", "驳回"),
    CANCEL("3", "撤销");

    private final String code;
    private final String description;

    ApprovalStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ApprovalStatus getByCode(String code) {
        for (ApprovalStatus status : ApprovalStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}

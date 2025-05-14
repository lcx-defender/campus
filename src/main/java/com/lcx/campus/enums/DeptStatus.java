package com.lcx.campus.enums;

/**
 * 部门状态:"0"停用,"1"正常
 *
 * @author 刘传星
 * @since 2025-05-14
 */
public enum DeptStatus {
    DISABLED("0"),
    ACTIVE("1");

    private final String code;

    DeptStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DeptStatus fromCode(String code) {
        for (DeptStatus status : DeptStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
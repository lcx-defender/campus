package com.lcx.campus.enums;

/**
 * 请假类型 "1" 住宿请假 "2" 课堂请假
 *
 * @author 刘传星
 * @since 2025-05-20
 */
public enum LeaveType {
    /**
     * 住宿请假
     */
    DORMITORY_LEAVE("1", "住宿请假"),

    /**
     * 课堂请假
     */
    CLASS_LEAVE("2", "课堂请假");

    private final String code;
    private final String message;

    LeaveType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    public static LeaveType getByCode(String code) {
        for (LeaveType leaveType : LeaveType.values()) {
            if (leaveType.getCode().equals(code)) {
                return leaveType;
            }
        }
        return null;
    }
}
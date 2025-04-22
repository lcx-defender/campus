package com.lcx.campus.enums;

/**
 * <p>
 *     字典状态
 * </p>
 * @author 刘传星
 * @since 2025-04-22
 */
public enum DictStatus {
    /**
     * 正常
     */
    NORMAL("1", "正常"),

    /**
     * 停用
     */
    DISABLE("0", "停用");

    private final String code;
    private final String message;

    DictStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

package com.lcx.campus.enums;

/**
 * 用户性别（0男 1女 2未知）
 *
 * @author 刘传星
 * @since 2025-05-16
 */
public enum SexType {
    MALE("0", "男"),
    FEMALE("1", "女"),
    UNKNOWN("2", "未知");

    private final String code;
    private final String description;

    SexType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getCodeByDescription(String description) {
        for (SexType sexType : values()) {
            if (sexType.getDescription().equals(description)) {
                return sexType.getCode();
            }
        }
        return null;
    }

    public static String getDescriptionByCode(String code) {
        for (SexType sexType : values()) {
            if (sexType.getCode().equals(code)) {
                return sexType.getDescription();
            }
        }
        return null;
    }
}
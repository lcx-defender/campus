package com.lcx.campus.enums;

/**
 * 菜单状态
 *
 * @author 刘传星
 * @since 2025-05-12
 */
public enum MenuStatus {
    // 0 正常 1 停用
    NORMAL("0", "正常"),
    DISABLE("1", "停用");

    private final String label;

    private final String value;

    MenuStatus(String value, String label) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}

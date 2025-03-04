package com.lcx.campus.enums;

/**
 * <p>
 * 业务操作类型
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public enum UserType {
    // 0系统管理&程序员;1教师;2学生
    SYSTEM("系统管理&程序员"),
    TEACHER("教师"),
    STUDENT("学生");

    private final String name;

    UserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

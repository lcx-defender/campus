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
    SYSTEM("0", "系统管理&程序员"),
    TEACHER("1","教师"),
    STUDENT("2", "学生");

    private final String name;

    private String code;

    UserType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getUserTypeByName(String name) {
        for (UserType userType : UserType.values()) {
            if (userType.getName().equals(name)) {
                return userType.getCode();
            }
        }
        return null;
    }

}

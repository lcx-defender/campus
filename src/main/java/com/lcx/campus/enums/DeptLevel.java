package com.lcx.campus.enums;

public enum DeptLevel {
    // 所处层级, 1:学校, 2:学院, 3:系, 4:专业，5:班级
    UNIVERSITY(1, "学校"),
    INSTITUTE(2, "学院"),
    DEPARTMENT(3, "系"),
    MAJOR(4, "专业"),
    CLAZZ(5, "班级");
    private final int level;
    private final String name;

    DeptLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    // 根据level获取对应的DeptLevel
    public static DeptLevel getByLevel(int level) {
        for (DeptLevel deptLevel : values()) {
            if (deptLevel.getLevel() == level) {
                return deptLevel;
            }
        }
        return null; // 或者抛出异常
    }
}

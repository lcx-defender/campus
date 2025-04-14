package com.lcx.campus.enums;

public enum PositionStatus {
    /**
     * 职位状态（0在职、1停职、2离职） 数据字典
     */
    ON_JOB("在职"), SUSPENSION("停职"), LEAVE("离职");

    private final String name;

    PositionStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

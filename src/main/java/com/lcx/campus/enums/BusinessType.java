package com.lcx.campus.enums;

/**
 * <p>
 * 业务操作类型
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public enum BusinessType {
    // 0其它 1新增 2修改 3删除 4查询 5授权 6导出 7导入 8强退
    OTHER("其它"),
    INSERT("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    QUERY("查询"),
    GRANT("授权"),
    EXPORT("导出"),
    IMPORT("导入"),
    FORCE("强退");

    private final String name;

    BusinessType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

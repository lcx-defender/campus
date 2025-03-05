package com.lcx.campus.enums;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户状态
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-05
 */
public enum UserStatus {
    // 0正常 1封禁 2删除
    OK("0", "正常"), DISABLE("1", "封禁"), DELETED("2", "删除");

    private final String code;
    private final String info;

    UserStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}

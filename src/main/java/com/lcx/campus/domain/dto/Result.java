package com.lcx.campus.domain.dto;

import com.lcx.campus.constant.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lcx.campus.constant.HttpStatus.FAIL;
import static com.lcx.campus.constant.HttpStatus.SUCCESS;

/**
 * <p>
 * 请求响应
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    // 返回的消息
    private String message;
    // 返回的数据
    private Object data;
    private Long total;

    public static Result success() {
        return new Result(SUCCESS, "success", null, null);
    }

    public static Result success(Object data) {
        return new Result(SUCCESS, "success", data, null);
    }
    public static Result success(String message, Object data) {
        return new Result(SUCCESS, message, data, null);
    }

    public static Result success(String message, Object data, Long total) {
        return new Result(SUCCESS, message, data, total);
    }

    public static Result fail() {
        return new Result(FAIL, "fail", null, null);
    }

    public static Result fail(String message) {
        return new Result(FAIL, message, null, null);
    }

    public static Result fail(Integer code, String message) {
        return new Result(code, message, null, null);
    }
}

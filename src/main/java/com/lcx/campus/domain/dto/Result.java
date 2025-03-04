package com.lcx.campus.domain.dto;

import com.lcx.campus.constant.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        return new Result(HttpStatus.SUCCESS, "success", null, null);
    }

    public static Result success(Object data) {
        return new Result(HttpStatus.SUCCESS, "success", data, null);
    }

    public static Result success(Object data, Long total) {
        return new Result(HttpStatus.SUCCESS, "success", data, total);
    }

    public static Result fail() {
        return new Result(400, "fail", null, null);
    }

    public static Result fail(String message) {
        return new Result(HttpStatus.ERROR, message, null, null);
    }

    public static Result fail(Integer code, String message) {
        return new Result(code, message, null, null);
    }
}

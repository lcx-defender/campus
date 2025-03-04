package com.lcx.campus.exception;

import java.io.Serial;

/**
 * <p>
 * 基础异常类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }
}

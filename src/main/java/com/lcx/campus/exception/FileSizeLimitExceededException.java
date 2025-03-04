package com.lcx.campus.exception;

public class FileSizeLimitExceededException extends BaseException {

    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(Long defaultMaxSize) {
        super("文件大小超过限制" + defaultMaxSize + "字节");
    }
}

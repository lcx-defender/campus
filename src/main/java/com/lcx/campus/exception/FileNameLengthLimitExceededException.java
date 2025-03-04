package com.lcx.campus.exception;


public class FileNameLengthLimitExceededException extends BaseException {

    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("文件名称长度超过限制" + defaultFileNameLength + "个字符");
    }
}

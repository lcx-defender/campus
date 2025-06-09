package com.lcx.campus.utils;

import cn.hutool.core.util.StrUtil;
import com.lcx.campus.exception.FileNameLengthLimitExceededException;
import com.lcx.campus.exception.FileSizeLimitExceededException;
import com.lcx.campus.exception.InvalidExtensionException;
import org.apache.commons.io.FilenameUtils;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * <p>
 * 文件上传工具类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public class FileUploadUtils {
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024L;
    /**
     * 默认的文件名最大长度 200 个字符
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 200;


    /**
     * 根据文件路径上传
     *
     * @param baseDir            相对应用的基目录
     * @param file               上传的文件
     * @param fileStorageService 文件存储服务
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file, FileStorageService fileStorageService)
            throws IOException {
        try {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, fileStorageService);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir          相对应用的基目录
     * @param file             上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException       如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws InvalidExtensionException            文件校验异常
     */
    public static String upload(String baseDir, MultipartFile file, String[] allowedExtension, FileStorageService fileStorageService)
            throws FileSizeLimitExceededException, FileNameLengthLimitExceededException, InvalidExtensionException {
        // 文件参数校验-长度、大小、类型
        assertAllowed(file, allowedExtension);
        String pathName = extractFileDir(baseDir);
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath(pathName)
                .upload();

        return fileInfo.getUrl();
    }

    private static void assertAllowed(MultipartFile file, String[] allowedExtension) {
        // 文件名长度校验
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(DEFAULT_FILE_NAME_LENGTH);
        }
        long size = file.getSize();
        if (size > DEFAULT_MAX_SIZE) {
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE);
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            throw new InvalidExtensionException(allowedExtension, extension, fileName);
        }
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StrUtil.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }

    public static final String extractFileDir(String baseDir) {
        // baseDir/yyyy/MM/dd
        return baseDir + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/";
    }
}

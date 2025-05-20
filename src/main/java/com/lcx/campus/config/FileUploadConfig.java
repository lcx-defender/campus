package com.lcx.campus.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置单个文件的最大大小
        factory.setMaxFileSize(DataSize.ofMegabytes(10)); // 10MB
        // 设置整个请求的最大大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(20)); // 20MB
        return factory.createMultipartConfig();
    }
}

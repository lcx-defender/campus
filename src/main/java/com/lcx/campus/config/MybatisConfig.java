package com.lcx.campus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Mybatis配置
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Configuration
public class MybatisConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor(DbType.MYSQL); // mysql数据库
        pageInterceptor.setMaxLimit(1000L); // 设置分页查询最大上限
        interceptor.addInnerInterceptor(pageInterceptor);
        return interceptor;
    }
}

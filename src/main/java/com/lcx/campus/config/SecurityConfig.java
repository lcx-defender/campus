package com.lcx.campus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <p>
 * spring-security配置
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Configuration
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity)
            throws Exception {
        return httpSecurity
                // 配置关闭csrf
                .csrf(csrf -> csrf.disable())
                // 基于token，所以不需要session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> {
                    // 对于登录login 注册register 验证码captchaImage 允许匿名访问
                    requests.requestMatchers("/user/login", "/user/register", "/captchaImage").permitAll()
                            // 静态资源，可匿名访问
                            .requestMatchers(HttpMethod.GET, "/", "/*.html", "/**.html", "/**.css", "/**.js", "/profile/**").permitAll()
                            .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/druid/**")
                            .permitAll()
                            // 除上面外的所有请求全部需要鉴权认证
                            .anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

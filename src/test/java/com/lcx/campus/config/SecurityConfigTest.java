package com.lcx.campus.config;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootTest
class SecurityConfigTest {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPasswordEncoder() {
        String password = "admin123456";
        String encode = passwordEncoder.encode(password);
        System.out.println(encode);
    }

    @Test
    public void testEquals() {
        String pwd = "$2a$10$SGoPUqmcd56FzbzsjYScCOMlPsnA1.4L0rjxl3GQpCZLid9yOmiKi";
        boolean isOk = passwordEncoder.matches("admin123456", pwd);
        System.out.println(isOk);
    }
}
package com.lcx.campus.service;

import com.lcx.campus.service.impl.JwtTokenServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class JwtTokenServiceImplTest {

    @Resource
    private JwtTokenServiceImpl jwtTokenServiceImpl;

    @Test
    void createToken() {
        Map<String, Object> claims = new ConcurrentHashMap<>();
        claims.put("username", "admin");
        String token = jwtTokenServiceImpl.createToken(claims);
        System.out.println(token);
    }

    @Test
    void parseToken() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3NDEwODgzNzgsImlhdCI6MTc0MTA3MDM3OCwidXNlcm5hbWUiOiJhZG1pbiJ9.ExkE58MshUxfycUOQ87ivIeUiEFeKcwbp6tEzS0fLdhufkT2LXbsnTMo0lZ6alqkBxY2SGPT0bozrLikbdMzQQ";
        Map<String, Object> map = jwtTokenServiceImpl.parseToken(token);
        System.out.println(map);
    }
}
package com.lcx.campus.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginBody {

    // 登录账户名
    private String username;

    // 学号信息
    private String studentId;

    // 登录密码
    private String password;

    // 验证码uuid
    private String uuid;

    // 验证码
    private String code;
}

package com.lcx.campus.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 注册请求体
 */
@Data
public class StudentRegisterBody {
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户类型（0系统管理&程序员;1教师;2学生）
     */
    private String userType;
    /**
     * 密码
     */
    @NotNull
    private String password;
    /**
     * 身份证号
     */
    @NotNull(message = "身份证号不能为空")
    private String identity;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 用户性别（0男 1女 2未知）数据字典
     */
    private String sex;
    /**
     * 真实姓名
     */
    @NotNull(message = "姓名不能为空")
    private String name;
    /**
     * 学校代码
     */
    @NotNull(message = "学校代码不能为空")
    private Long universityId;
    /**
     * 学院代码
     */
    private Long instituteId;
    /**
     * 专业代码
     */
    private Long majorId;
    /**
     * 当前年级
     */
    private String currentGrade;
    /**
     * 学籍状态（0未注册、1在籍、2保留学籍、3休学、4退学、5毕业、6结业、7肄业） 数据字典
     */
    private Integer academicStatus;
}
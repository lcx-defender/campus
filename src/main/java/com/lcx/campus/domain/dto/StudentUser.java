package com.lcx.campus.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcx.campus.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生用户整体封装
 *
 * @author 刘传星
 * @since 2025-05-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUser extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @ExcelIgnore
    private Long userId;
    /**
     * 用户昵称
     */
    @ExcelProperty("用户昵称")
    private String nickname;
    /**
     * 用户类型（0系统管理&程序员;1教师;2学生） 数据字典
     */
    @ExcelIgnore
    private String userType;
    /**
     * 身份证号
     */
    @ExcelProperty("身份证号")
    @NotNull(message = "身份证号不能为空")
    private String identity;
    /**
     * 用户邮箱
     */
    @ExcelProperty("用户邮箱")
    private String email;
    /**
     * 手机号码
     */
    @ExcelProperty("手机号码")
    private String phone;
    /**
     * 用户性别（0男 1女 2未知）数据字典
     */
    @ExcelProperty("用户性别")
    @NotNull(message = "用户性别不能为空")
    private String sex;
    /**
     * 头像地址
     */
    @ExcelIgnore
    private String avatar;
    /**
     * 密码
     */
    @ExcelProperty("密码")
    @NotNull(message = "密码不能为空")
    private String password;
    /**
     * 帐号状态（0正常 1封禁 2删除）数据字典
     */
    @ExcelProperty("帐号状态")
    private String userStatus;
    /**
     * 学号
     */
    @ExcelProperty("学号")
    private String studentId;
    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    private String studentName;
    /**
     * 查询条件：deptId
     */
    @ExcelIgnore
    private Long deptId;
    /**
     * 学校代码
     */
    @ExcelProperty("学校代码")
    private Long universityId;
    /**
     * 学院代码
     */
    @ExcelProperty("学院代码")
    private Long instituteId;
    /**
     * 专业代码
     */
    @ExcelProperty("专业代码")
    private Long majorId;
    /**
     * 班级代码
     */
    @ExcelProperty("班级代码")
    private Long classId;
    /**
     * 当前年级
     */
    @ExcelProperty("当前年级")
    private String currentGrade;
    /**
     * 入学时间
     */
    @ExcelProperty("入学时间")
    private LocalDateTime admitTime;
    /**
     * 毕业时间
     */
    @ExcelProperty("毕业时间")
    private LocalDateTime graduateTime;
    /**
     * 政治面貌
     */
    @ExcelProperty("政治面貌")
    private String politicalStatus;
    /**
     * 民族
     */
    @ExcelProperty("民族")
    private String ethnic;
    /**
     * 学籍状态（0未注册、1在籍、2保留学籍、3休学、4退学、5毕业、6结业、7肄业） 数据字典
     */
    @ExcelProperty("学籍状态")
    private Integer academicStatus;
    /**
     * 高中就读学校
     */
    private String highSchool;
    /**
     * 家庭住址
     */
    private String homeAddress;
}
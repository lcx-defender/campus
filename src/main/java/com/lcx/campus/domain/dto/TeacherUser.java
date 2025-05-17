package com.lcx.campus.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lcx.campus.domain.Teacher;
import com.lcx.campus.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教师用户信息
 *
 * @author 刘传星
 * @since 2025-05-16
 */
@Data
public class TeacherUser extends PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    // 用户部分
    private Long userId;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户类型（0系统管理&程序员;1教师;2学生） 数据字典
     */
    private String userType;
    /**
     * 身份证号
     */
    @NotNull(message = "身份证号不能为空", groups = {User.AddUserGroup.class})
    private String identity;
    /**
     * 用户邮箱
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
     * 头像地址
     */
    private String avatar;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空", groups = {User.AddUserGroup.class})
    private String password;
    /**
     * 帐号状态（0正常 1封禁 2删除）数据字典
     */
    private String userStatus;
    // 教师信息部分
    /**
     * 教师工号
     */
    private String teacherId;
    /**
     * 姓名
     */
    @NotNull(groups = {Teacher.addTeacher.class}, message = "教师姓名不能为空")
    private String teacherName;
    /**
     * 部门代码
     * 直接显示最低单位,校级老师显示校级id,院级老师显示院级id
     */
    @NotNull(groups = {Teacher.addTeacher.class}, message = "部门ID不能为空")
    private Long deptId;
    /**
     * 职称
     */
    private String title;
    /**
     * 办公室
     */
    private String office;
    /**
     * 入职时间
     */
    private LocalDateTime admitTime;
    /**
     * 职位状态（0在职、1停职、2离职） 数据字典
     */
    private Integer positionStatus;
    /**
     * 用户注册时间
     */
    private LocalDateTime createTime;
}

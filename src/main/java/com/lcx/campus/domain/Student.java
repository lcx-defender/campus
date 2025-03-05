package com.lcx.campus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生表
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID,逻辑外键
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 姓名
     */
    private String studentName;

    /**
     * 学校代码
     */
    private String universityId;

    /**
     * 学院代码
     */
    private String instituteId;

    /**
     * 专业代码
     */
    private String majorId;

    /**
     * 班级代码
     */
    private String classId;

    /**
     * 当前年级
     */
    private String currentGrade;

    /**
     * 入学时间
     */
    private LocalDateTime admitTime;

    /**
     * 毕业时间
     */
    private LocalDateTime graduateTime;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 民族
     */
    private String ethnic;

    /**
     * 学籍状态（0未注册、1在籍、2保留学籍、3休学、4退学、5毕业、6结业、7肄业） 数据字典
     */
    private Integer academicStatus;

    /**
     * 高中
     */
    private String highSchool;

    /**
     * 家庭住址
     */
    private String homeAddress;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;


}

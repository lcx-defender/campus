package com.lcx.campus.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.lcx.campus.domain.query.PageQuery;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 教师表
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("teacher")
public class Teacher extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID,逻辑外键
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    @NotNull(groups = {addTeacher.class, updateInfo.class}, message = "用户ID不能为空")
    private Long userId;
    /**
     * 教师工号
     */
    private String teacherId;
    /**
     * 姓名
     */
    @NotNull(groups = {addTeacher.class}, message = "教师姓名不能为空")
    private String teacherName;
    /**
     * 部门代码
     * 直接显示最低单位,校级老师显示校级id,院级老师显示院级id
     */
    @NotNull(groups = {addTeacher.class}, message = "部门ID不能为空")
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
     * 创建者
     */
    private String createBy;
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
    public interface addTeacher {}
    public interface updateInfo {}
}

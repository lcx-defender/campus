package com.lcx.campus.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 院校表
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dept")
public class Dept implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 部门id,由创建者输入学校、学院、系、班级等编号
     */
    @TableId(value = "dept_id", type = IdType.AUTO)
    @NotNull(message = "部门id不能为空", groups = {Dept.update.class})
    private Long deptId;
    /**
     * 父部门id
     */
    @NotNull(message = "父部门不能为空", groups = {Dept.insert.class})
    private Long parentId;
    /**
     * 所处层级, 1:学校, 2:学院, 3:系, 4:专业，5:班级
     */
    private Integer level;
    /**
     * 子部门
     */
    @TableField(exist = false)
    private List<Dept> children = new ArrayList<>();
    /**
     * 部门名称
     */
    @NotNull(message = "部门名称不能为空", groups = {Dept.insert.class})
    private String deptName;
    /**
     * 负责人
     */
    private String leader;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /** 部门状态:0停用,1正常 */
    private String status;
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;
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
    public interface insert {}
    public interface update {}
}

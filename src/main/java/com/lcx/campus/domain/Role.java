package com.lcx.campus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.dto.PageQuery;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
public class Role extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名称
     */
    @NotNull(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色权限标识符
     */
    @NotNull(message = "角色权限标识符不能为空")
    private String roleKey;

    /**
     * 角色状态（0正常 1停用） 数据字典
     */
    private String roleStatus;

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

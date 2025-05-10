package com.lcx.campus.domain.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 *     角色授予菜单权限VO
 * </p>
 *
 * @author 刘传星
 * @since 2025-05-10
 */
@Data
public class RoleMenusVo {

    @NotNull(message = "角色id不能为空")
    private Long roleId;

    private Long[] menuIds;
}

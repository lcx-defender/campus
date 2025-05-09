package com.lcx.campus.domain.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 *     用户绑定的所有角色信息,用以更新用户的角色信息
 * </p>
 *
 * @author 刘传星
 * @since 2025-05-09
 */
@Data
public class UserRolesVo {
    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 角色id数组
     */
    private Long[] roleIds;
}

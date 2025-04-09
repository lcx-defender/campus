package com.lcx.campus.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 * 修改密码请求体
 * </p>
 *
 * @author 刘传星
 * @since 2025-04-09
 */
@Data
public class PasswordBody {

    @NotNull(groups = UserUpdateGroup.class)
    private String oldPassword;

    @NotNull(groups = {UserUpdateGroup.class, AdminResetGroup.class})
    private String newPassword;

    @NotNull(groups = UserUpdateGroup.class)
    private String confirmPassword;

    @NotNull(groups = AdminResetGroup.class)
    private Long userId;

    /**
     * 用户更新密码校验分组
     */
    public interface UserUpdateGroup {}

    /**
     * 管理员重置密码校验分组
     */
    public interface AdminResetGroup {}

}



package com.lcx.campus.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.lcx.campus.domain.dto.PageQuery;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class User extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    @NotNull(message = "用户ID不能为空", groups = {User.UpdateUserGroup.class})
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
    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录地点
     */
    private String loginLocation;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 注册时间
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

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private Set<Menu> menus;

    public interface AddUserGroup {}

    public interface UpdateUserGroup {}

    public interface UpdateAvatarGroup {}
}

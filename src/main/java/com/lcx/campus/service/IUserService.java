package com.lcx.campus.service;

import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.Teacher;
import com.lcx.campus.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.dto.LoginBody;
import com.lcx.campus.domain.dto.PasswordBody;
import com.lcx.campus.domain.dto.Result;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface IUserService extends IService<User> {
    User selectUserByUserName(String userName);

    Result loginByUsername(LoginBody user);

    Result updateUserProfile(User user);

    Result getSelfInfo();

    Result updatePassword(PasswordBody passwordBody, HttpServletRequest request);

    Result resetPassword(PasswordBody passwordBody);

    Result addUserOfAdmin(User user);

    /**
     * 创建用户如果不存在当前信息用户
     * @param user
     * @return 用户ID
     */
    Long creatUserIfNotExist(User user);

    /**
     * 用户修改个人信息，只允许个人修改 nickname、email、phone、sex 这四个字段，根据uderId更新
     */
    Result updateSelfInfo(User user);

    /**
     * 分页查询所有自己部门之下的用户信息
     * 通过用户昵称、用户类型、邮箱、手机号、用户状态去多条件查询
     */
    Result updateUser(User user);

    Result pageUserList(User user);
}

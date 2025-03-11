package com.lcx.campus.service;

import com.lcx.campus.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.dto.LoginBody;
import com.lcx.campus.domain.dto.Result;

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
}

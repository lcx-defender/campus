package com.lcx.campus.service;

import com.lcx.campus.domain.UserOnline;
import com.lcx.campus.domain.vo.Result;

/**
 * 在线用户
 *
 * @author 刘传星
 * @since 2025-05-14
 */
public interface IUserOnlineService {
    /**
     * 获取在线用户分页列表
     */
    Result getUserOnlinePage(UserOnline userOnline);

    /**
     * 强制退出用户
     */
    Result forceLogout(String tokenId);
}

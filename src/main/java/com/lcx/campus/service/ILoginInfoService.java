package com.lcx.campus.service;

import com.lcx.campus.domain.LoginInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.dto.Result;

/**
 * <p>
 * 系统登录记录 服务类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public interface ILoginInfoService extends IService<LoginInfo> {

    Result insertLoginInfo(LoginInfo loginInfo);
}

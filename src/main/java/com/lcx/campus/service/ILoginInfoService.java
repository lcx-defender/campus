package com.lcx.campus.service;

import com.lcx.campus.domain.LoginInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcx.campus.domain.vo.Result;

import java.util.List;

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

    Result deleteLoginInfo(Long[] infoIds);

    Result deleteLoginInfoBatch(List<Long> infoIds);

    Result getLoginInfoList(LoginInfo loginInfo);

    Result clearLoginInfo();
}

package com.lcx.campus.service.impl;

import com.lcx.campus.domain.LoginInfo;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.mapper.LoginInfoMapper;
import com.lcx.campus.service.ILoginInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统登录记录 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
public class LoginInfoServiceImpl extends ServiceImpl<LoginInfoMapper, LoginInfo> implements ILoginInfoService {

    @Override
    public Result insertLoginInfo(LoginInfo loginInfo) {
        boolean isSuccess = save(loginInfo);
        return isSuccess ? Result.success() : Result.fail();
    }
}

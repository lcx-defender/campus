package com.lcx.campus.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcx.campus.domain.LoginInfo;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.mapper.LoginInfoMapper;
import com.lcx.campus.service.ILoginInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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

    @Resource
    private LoginInfoMapper loginInfoMapper;

    @Override
    public Result insertLoginInfo(LoginInfo loginInfo) {
        boolean isSuccess = save(loginInfo);
        return isSuccess ? Result.success() : Result.fail();
    }

    @Override
    public Result deleteLoginInfo(Long[] infoIds) {
        boolean isSuccess = removeByIds(Arrays.asList(infoIds));
        return isSuccess ? Result.success("删除成功", null) : Result.fail("删除登录记录失败，请检查登录记录ID是否正确或记录是否存在");
    }

    @Override
    public Result deleteLoginInfoBatch(List<Long> infoIds) {
        boolean isSuccess = removeByIds(infoIds);
        return isSuccess ? Result.success("批量删除成功", null) : Result.fail("批量删除登录记录失败，请检查登录记录ID是否正确或记录是否存在");
    }

    @Override
    public Result getLoginInfoList(LoginInfo loginInfo) {
        Page<LoginInfo> queryPage = loginInfo.toMpPage();
        Page<LoginInfo> resPage = lambdaQuery()
                .eq(loginInfo.getUserId() != null, LoginInfo::getUserId, loginInfo.getUserId())
                .like(loginInfo.getLoginIp() != null, LoginInfo::getLoginIp, loginInfo.getLoginIp())
                .like(loginInfo.getLoginLocation() != null, LoginInfo::getLoginLocation, loginInfo.getLoginLocation())
                .like(loginInfo.getBrowser() != null, LoginInfo::getBrowser, loginInfo.getBrowser())
                .like(loginInfo.getOs() != null, LoginInfo::getOs, loginInfo.getOs())
                .eq(StringUtils.isNotEmpty(loginInfo.getLoginStatus()), LoginInfo::getLoginStatus, loginInfo.getLoginStatus())
                .page(queryPage);
        PageVo<LoginInfo> res = PageVo.of(resPage);
        return Result.success("查询成功", res);
    }

    @Override
    public Result clearLoginInfo() {
        loginInfoMapper.clearAll();
        return Result.success("清空登录记录成功", null);
    }
}

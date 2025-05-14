package com.lcx.campus.controller;


import com.lcx.campus.domain.LoginInfo;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.service.ILoginInfoService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统登录记录 前端控制器
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@RestController
@RequestMapping("/login-info")
public class LoginInfoController {

    @Resource
    private ILoginInfoService loginInfoService;

    /**
     * 分页查询全体登录记录
     */
    @PostMapping("/pageList")
    @PreAuthorize("hasAnyAuthority('monitor:logininfo:list')")
    public Result getLoginInfoList(@RequestBody LoginInfo loginInfo) {
        return loginInfoService.getLoginInfoList(loginInfo);
    }

    /**
     * 删除单个登录记录
     */
    @DeleteMapping("/delete/{infoIds}")
    @PreAuthorize("hasAnyAuthority('monitor:logininfo:remove')")
    public Result deleteLoginInfo(@PathVariable Long[] infoIds) {
        return loginInfoService.deleteLoginInfo(infoIds);
    }

    /**
     * 清空登录记录
     */
    @DeleteMapping("/clear")
    @PreAuthorize("hasAnyAuthority('monitor:logininfo:remove')")
    public Result clearLoginInfo() {
        return loginInfoService.clearLoginInfo();
    }
}

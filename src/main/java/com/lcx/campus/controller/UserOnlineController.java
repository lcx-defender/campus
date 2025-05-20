package com.lcx.campus.controller;

import com.lcx.campus.annotation.Log;
import com.lcx.campus.domain.UserOnline;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.enums.BusinessType;
import com.lcx.campus.service.IUserOnlineService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户在线监控
 *
 * @author 刘传星
 * @since 2025-05-14
 */
@RestController
@RequestMapping("/user-online")
public class UserOnlineController {

    @Resource
    private IUserOnlineService userOnlineService;

    /**
     * 获取在线用户分页列表
     */
    @PreAuthorize("hasAnyAuthority('monitor:online:list')")
    @PostMapping("/getPageList")
    @Log(title = "获取在线用户分页列表", businessType = BusinessType.QUERY)
    public Result getUserOnlinePage(@RequestBody UserOnline userOnline) {
        return userOnlineService.getUserOnlinePage(userOnline);
    }

    /**
     * 强制退出用户
     */
    @PreAuthorize("hasAnyAuthority('monitor:online:forceLogout')")
    @Log(title = "强制退出用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/forceLogout/{tokenId}")
    public Result forceLogout(@PathVariable String tokenId) {
        return userOnlineService.forceLogout(tokenId);
    }
}

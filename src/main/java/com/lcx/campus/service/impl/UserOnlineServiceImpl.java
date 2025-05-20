package com.lcx.campus.service.impl;

import com.alibaba.fastjson2.JSON;
import com.lcx.campus.constant.RedisConstants;
import com.lcx.campus.domain.UserOnline;
import com.lcx.campus.domain.dto.LoginUser;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.domain.vo.PageVo;
import com.lcx.campus.service.IUserOnlineService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserOnlineServiceImpl implements IUserOnlineService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private JwtTokenServiceImpl jwtTokenService;

    @Override
    public Result getUserOnlinePage(UserOnline userOnline) {

        String tokenKey = RedisConstants.LOGIN_KEY + "*";
        Set<String> keys = stringRedisTemplate.keys(tokenKey);
        List<UserOnline> onlineUsers = new ArrayList<>();
        for (String key : keys) {
            String JsonStr = stringRedisTemplate.opsForValue().get(key);
            LoginUser loginUser = JSON.parseObject(JsonStr, LoginUser.class);
            System.out.println("loginUser = " + loginUser);
            // 根据前端传来的参数进行筛选：主要为 userId 和 loginLocation
            if (userOnline.getUserId() != null && !userOnline.getUserId().equals(loginUser.getUser().getUserId())) {
                continue;
            }
            if (userOnline.getLoginLocation() != null && !userOnline.getLoginLocation().equals(loginUser.getLoginLocation())) {
                continue;
            }
            // 将 LoginUser 转换为 UserOnline
            onlineUsers.add(convertToUserOnline(loginUser));
        }
        // 将在线用户列表转换为分页对象
        PageVo<UserOnline> res = new PageVo<>();
        int pageNo = userOnline.getPageNo();
        int pageSize = userOnline.getPageSize();
        int total = onlineUsers.size();
        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        if (startIndex >= total) {
            return Result.fail("没有更多数据了");
        }
        res.setTotal((long) total);
        res.setPages((long) (onlineUsers.size() / pageSize));
        // onlineUsers 截取分页
        res.setList(onlineUsers.subList(startIndex, endIndex));
        return Result.success(res);
    }

    @Override
    public Result forceLogout(String tokenId) {
        Boolean isSuccess = stringRedisTemplate.delete(RedisConstants.LOGIN_KEY + tokenId);
        return isSuccess ? Result.success("强制退出成功", null) : Result.fail("强制退出失败");
    }

    private UserOnline convertToUserOnline(LoginUser loginUser) {
        UserOnline userOnline = new UserOnline();
        userOnline.setTokenId(loginUser.getTokenUUID());
        userOnline.setUserId(loginUser.getUser().getUserId());
        userOnline.setIpaddr(loginUser.getIpAddress());
        userOnline.setLoginLocation(loginUser.getLoginLocation());
        userOnline.setBrowser(loginUser.getBrowser());
        userOnline.setOs(loginUser.getOs());
        userOnline.setLoginTime(loginUser.getLoginTime());
        return userOnline;
    }
}

package com.lcx.campus.utils;

import com.lcx.campus.constant.Constants;
import com.lcx.campus.domain.LoginInfo;
import com.lcx.campus.domain.OperateLog;
import com.lcx.campus.service.ILoginInfoService;
import com.lcx.campus.service.IOperateLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author 刘传星
 * @since 2025-03-05
 */
public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param userId  用户ID
     * @param status  状态
     * @param message 消息
     * @param args    列表
     * @return 任务task
     */
    public static TimerTask recordLoginInfo(final Long userId, final String status, final String message,
                                            final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                LoginInfo loginInfo = new LoginInfo();
                loginInfo.setUserId(userId);
                loginInfo.setLoginIp(ip);
                loginInfo.setLoginLocation(address);
                loginInfo.setBrowser(browser);
                loginInfo.setOs(os);
                loginInfo.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    loginInfo.setLoginStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    loginInfo.setLoginStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ILoginInfoService.class).insertLoginInfo(loginInfo);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operateLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOperate(final OperateLog operateLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operateLog.setOperateLocation(AddressUtils.getRealAddressByIP(operateLog.getOperateIp()));
                SpringUtils.getBean(IOperateLogService.class).insertOperateLog(operateLog);
            }
        };
    }
}

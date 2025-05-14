package com.lcx.campus.domain;

import com.lcx.campus.domain.dto.PageQuery;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserOnline extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话编号
     */
    private String tokenId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地址
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录时间
     */
    private Long loginTime;
}

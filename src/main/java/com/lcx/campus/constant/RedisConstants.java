package com.lcx.campus.constant;

/**
 * <p>
 * Redis常量
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
public class RedisConstants {

    /**
     * 登录token前缀
     */
    public static final String LOGIN_KEY = "login_tokenUUID:";

    /**
     * 验证码前缀
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_code:";

    /**
     * dict数据字典缓存前缀
     */
    public static final String SYS_DICT_KEY = "sys_dict_type:";
}

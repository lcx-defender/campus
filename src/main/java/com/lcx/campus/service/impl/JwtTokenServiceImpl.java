package com.lcx.campus.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.lcx.campus.domain.dto.LoginUser;
import com.lcx.campus.utils.AddressUtils;
import com.lcx.campus.utils.IpUtils;
import com.lcx.campus.utils.ServletUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.lcx.campus.constant.Constants.JWT_CLAIMS;
import static com.lcx.campus.constant.RedisConstants.LOGIN_KEY;

/**
 * <p>
 * Jwt工具类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
@Slf4j
public class JwtTokenServiceImpl {

    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expireTime}")
    private Long expireTime;

    /**
     * 一秒
     */
    protected static final Long SECOND_UNIT = 1000L;
    /**
     * 一分
     */
    protected static final Long MINUTE_UNIT = 60 * SECOND_UNIT;
    /**
     * 一小时
     */
    private static final Long HOUR_UNIT = 60 * MINUTE_UNIT;

    /**
     * 刷新时间
     */
    private static final Long REFRESH_TIME = 30 * MINUTE_UNIT;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String tokenUUID = StrUtil.uuid();
        loginUser.setTokenUUID(tokenUUID);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JWT_CLAIMS, tokenUUID);
        return createToken(claims);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 令牌载荷
     * @return 令牌
     */
    public String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * HOUR_UNIT);

        String tokenKey = getTokenKey(loginUser.getTokenUUID());
        stringRedisTemplate.opsForValue().set(tokenKey, JSON.toJSONString(loginUser), expireTime, TimeUnit.MINUTES);
    }

    /**
     * 获取存储到Redis中的键值
     *
     * @param tokenUUID
     * @return
     */
    private String getTokenKey(String tokenUUID) {
        return LOGIN_KEY + tokenUUID;
    }

    /**
     * 设置登录用户的设备信息
     *
     * @param loginUser
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr();
        loginUser.setIpAddress(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 验证令牌有效期，相差不足30分钟，自动刷新缓存
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if(expireTime <= currentTime) {
            throw new BadCredentialsException("token已过期");
        }else if (expireTime - currentTime <= REFRESH_TIME) {
            refreshToken(loginUser);
        }
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户登录信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String uuid = parseTokenToUUID(request);
        if (StrUtil.isEmpty(uuid)) {
            return null;
        }
        String userKey = getTokenKey(uuid);
        String JsonStr = stringRedisTemplate.opsForValue().get(userKey);
        if (StrUtil.isEmpty(JsonStr)) {
            return null;
        }
        return JSON.parseObject(JsonStr, LoginUser.class);
    }

    /**
     * @param request
     * @return 解析token获取UUID，方便从Redis中获取LoginUser
     */
    public String parseTokenToUUID(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        return parseToken(request).get(JWT_CLAIMS).toString();
    }

    /**
     * 根据前端请求头解析token并获取其中载荷map
     * @param request 请求
     * @return 解析token获取载荷
     */
    public Map<String, Object> parseToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        return parseToken(token);
    }

    /**
     * 解析token，获取token载荷数据
     *
     * @param token 用户令牌
     * @return token载荷数据
     */
    public Map<String, Object> parseToken(String token) {
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String tokenUUID) {
        if (StrUtil.isNotEmpty(tokenUUID)) {
            String userKey = getTokenKey(tokenUUID);
            stringRedisTemplate.delete(userKey);
        }
    }
}

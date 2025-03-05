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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.lcx.campus.constant.RedisConstants.LOGIN_KEY;

/**
 * <p>
 * Jwt工具类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Component
public class JwtTokenServiceImpl {

    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expireTime}")
    private Long expireTime;

    protected static final Long MILLIS_SECOND = 1000L;

    protected static final Long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

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
        claims.put(LOGIN_KEY, tokenUUID);
        return createToken(claims);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;
    }

    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);

        String tokenKey = getTokenKey(loginUser.getTokenUUID());
        stringRedisTemplate.opsForValue().set(tokenKey, JSON.toJSONString(loginUser), expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String tokenUUID) {
        return LOGIN_KEY + tokenUUID;
    }

    public void setUserAgent(LoginUser loginUser)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr();
        loginUser.setIpAddress(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }



    // 接收token,验证token,并返回业务数据
    public Map<String, Object> parseToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        return parseToken(token);
    }

    public Map<String, Object> parseToken(String token) {
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}

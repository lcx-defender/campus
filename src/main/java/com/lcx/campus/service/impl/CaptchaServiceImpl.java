package com.lcx.campus.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.lcx.campus.domain.vo.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.lcx.campus.constant.Constants.CAPTCHA_EXPIRATION;
import static com.lcx.campus.constant.RedisConstants.CAPTCHA_CODE_KEY;

/**
 * <p>
 * 验证码实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-06
 */
@Service
public class CaptchaServiceImpl {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Result getCode(HttpServletResponse response) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        String uuid = IdUtil.simpleUUID();
        String code = lineCaptcha.getCode();
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        lineCaptcha.write(os);
        // 对字节数组Base64编码
        Map<String, String> result = new ConcurrentHashMap<>();
        result.put("uuid", uuid);
        result.put("img", Base64.encode(os.toByteArray()));
        // 保存验证码信息
        String key = CAPTCHA_CODE_KEY + uuid;
        stringRedisTemplate.opsForValue().set(key, code, CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        System.out.println("验证码：" + code);
        return Result.success(result);
    }
}

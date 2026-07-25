package com.iy.api.aspect;

import com.iy.api.common.annotation.RateLimit;
import com.iy.api.common.enums.BizCodeEnum;
import com.iy.api.common.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String RATE_LIMIT_PREFIX = "rate_limit:";

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = buildKey(joinPoint, rateLimit);
        Long currentCount = redisTemplate.opsForValue().increment(key);

        if (currentCount != null && currentCount == 1) {
            redisTemplate.expire(key, rateLimit.windowSeconds(), TimeUnit.SECONDS);
        }

        if (currentCount != null && currentCount > rateLimit.maxRequests()) {
            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            log.warn("Rate limit exceeded: key={}, current={}, max={}, ttl={}s",
                    key, currentCount, rateLimit.maxRequests(), ttl);
            throw new BizException(BizCodeEnum.LOGIN_RATE_LIMIT.getCode(), rateLimit.message());
        }

        return joinPoint.proceed();
    }

    private String buildKey(ProceedingJoinPoint joinPoint, RateLimit rateLimit) {
        StringBuilder keyBuilder = new StringBuilder(RATE_LIMIT_PREFIX);

        if (!rateLimit.key().isEmpty()) {
            keyBuilder.append(rateLimit.key());
        } else {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            keyBuilder.append(signature.getDeclaringTypeName())
                      .append(".")
                      .append(signature.getMethod().getName());
        }

        keyBuilder.append(":");

        switch (rateLimit.type()) {
            case IP -> keyBuilder.append(getClientIp());
            case ACCOUNT -> keyBuilder.append(extractAccount(joinPoint));
            case GLOBAL -> keyBuilder.append("global");
        }

        return keyBuilder.toString();
    }

    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "unknown";
        }
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String extractAccount(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        if (paramNames == null || args == null) {
            return "unknown";
        }
        for (int i = 0; i < paramNames.length; i++) {
            if (args[i] == null) continue;
            if ("account".equalsIgnoreCase(paramNames[i])) {
                return args[i].toString();
            }
            String value = getFieldValue(args[i], "account");
            if (value != null) {
                return value;
            }
        }
        return "unknown";
    }

    private String getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(obj);
            return value != null ? value.toString() : null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}

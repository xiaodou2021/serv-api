package com.iy.api.common.annotation;

import com.iy.api.common.enums.RateLimitType;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    int maxRequests() default 5;

    int windowSeconds() default 60;

    RateLimitType type() default RateLimitType.IP;

    String key() default "";

    String message() default "请求过于频繁，请稍后再试";
}

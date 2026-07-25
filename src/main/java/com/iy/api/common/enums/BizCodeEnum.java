package com.iy.api.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizCodeEnum {

    SUCCESS("200", "操作成功"),

    PARAM_INVALID("400", "参数校验失败"),
    UNAUTHORIZED("401", "未认证或认证已过期"),
    FORBIDDEN("403", "无权限访问"),
    NOT_FOUND("404", "资源不存在"),

    ACCOUNT_EXISTS("1001", "账号已存在"),
    ACCOUNT_NOT_FOUND("1002", "账号不存在"),
    PASSWORD_ERROR("1003", "密码错误"),
    ACCOUNT_LOCKED("1004", "账号已被锁定"),
    TOKEN_INVALID("1005", "Token无效或已过期"),
    TOKEN_EXPIRED("1006", "Token已过期，请重新登录"),

    RATE_LIMIT_EXCEEDED("2001", "请求过于频繁，请稍后再试"),
    LOGIN_RATE_LIMIT("2002", "登录尝试次数过多，请稍后再试"),

    SYSTEM_ERROR("500", "系统内部错误"),
    SERVICE_UNAVAILABLE("503", "服务暂时不可用，请稍后重试"),
    DB_ERROR("504", "数据库操作异常"),
    CACHE_ERROR("505", "缓存操作异常");

    private final String code;
    private final String message;
}

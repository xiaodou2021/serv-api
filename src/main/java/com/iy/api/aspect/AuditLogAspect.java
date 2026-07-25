package com.iy.api.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iy.api.common.annotation.AuditLog;
import com.iy.api.model.entity.SysAuditLogEntity;
import com.iy.api.model.security.LoginUser;
import com.iy.api.service.ISysAuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Aspect
@Component
@Order(2)
@Slf4j
@RequiredArgsConstructor
public class AuditLogAspect {

    private final ISysAuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    private static final int MAX_PARAMS_LENGTH = 2000;

    @Around("@annotation(auditLog)")
    public Object around(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        long startTime = System.currentTimeMillis();

        SysAuditLogEntity logEntity = new SysAuditLogEntity();
        fillUserInfo(logEntity);
        fillRequestInfo(logEntity, auditLog);

        if (auditLog.saveParams()) {
            logEntity.setRequestParams(serializeArgs(joinPoint.getArgs()));
        }

        Object result = null;
        Throwable exception = null;
        try {
            result = joinPoint.proceed();
            logEntity.setSuccess(1);
            if (auditLog.saveResult() && result != null) {
                logEntity.setResponseResult(serializeResult(result));
            }
            return result;
        } catch (Throwable e) {
            exception = e;
            logEntity.setSuccess(0);
            logEntity.setErrorMessage(e.getMessage());
            throw e;
        } finally {
            logEntity.setDuration(System.currentTimeMillis() - startTime);
            logEntity.setCreateTime(new Date());
            auditLogService.saveAuditLogAsync(logEntity);

            if (exception != null) {
                log.warn("Audit [FAIL] module={}, type={}, desc={}, duration={}ms, error={}",
                        logEntity.getModule(), logEntity.getOperationType(),
                        logEntity.getDescription(), logEntity.getDuration(), exception.getMessage());
            } else {
                log.info("Audit [OK] module={}, type={}, desc={}, duration={}ms",
                        logEntity.getModule(), logEntity.getOperationType(),
                        logEntity.getDescription(), logEntity.getDuration());
            }
        }
    }

    private void fillUserInfo(SysAuditLogEntity logEntity) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
                logEntity.setUserId(loginUser.getUser().getUserId());
                logEntity.setAccount(loginUser.getUser().getAccount());
            }
        } catch (Exception e) {
            log.debug("Unable to get current user for audit log");
        }
    }

    private void fillRequestInfo(SysAuditLogEntity logEntity, AuditLog auditLog) {
        logEntity.setModule(auditLog.module());
        logEntity.setOperationType(auditLog.type().name());
        logEntity.setDescription(auditLog.description());

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                logEntity.setRequestMethod(request.getMethod());
                logEntity.setRequestUri(request.getRequestURI());
                logEntity.setRequestIp(getClientIp(request));
            }
        } catch (Exception e) {
            log.debug("Unable to get request info for audit log");
        }
    }

    private String getClientIp(HttpServletRequest request) {
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

    private String serializeArgs(Object[] args) {
        try {
            String json = objectMapper.writeValueAsString(args);
            if (json.length() > MAX_PARAMS_LENGTH) {
                return json.substring(0, MAX_PARAMS_LENGTH) + "...(truncated)";
            }
            return json;
        } catch (Exception e) {
            log.debug("Failed to serialize request args");
            return null;
        }
    }

    private String serializeResult(Object result) {
        try {
            String json = objectMapper.writeValueAsString(result);
            if (json.length() > MAX_PARAMS_LENGTH) {
                return json.substring(0, MAX_PARAMS_LENGTH) + "...(truncated)";
            }
            return json;
        } catch (Exception e) {
            log.debug("Failed to serialize response result");
            return null;
        }
    }
}

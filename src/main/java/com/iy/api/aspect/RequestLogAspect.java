package com.iy.api.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class RequestLogAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.iy.api.controller..*.*(..))")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        String method = request.getMethod();
        String path = request.getRequestURI();
        String queryString = request.getQueryString();
        
        Map<String, Object> params = new HashMap<>();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        
        if (paramNames != null && paramValues != null) {
            for (int i = 0; i < paramNames.length; i++) {
                Object value = paramValues[i];
                if (value != null && !isFilteredType(value.getClass())) {
                    params.put(paramNames[i], value);
                }
            }
        }
        
        log.info("Request - Method: {}, Path: {}, Query: {}, Params: {}", 
                method, path, queryString, params);
        
        Object result = null;
        Throwable exception = null;
        
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            exception = e;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String resultStr = null;
            
            if (result != null) {
                try {
                    resultStr = objectMapper.writeValueAsString(result);
                    if (resultStr.length() > 2000) {
                        resultStr = resultStr.substring(0, 2000) + "...(truncated)";
                    }
                } catch (Exception e) {
                    resultStr = result.toString();
                }
            }
            
            if (exception != null) {
                log.warn("Response - Method: {}, Path: {}, Duration: {}ms, Exception: {}", 
                        method, path, duration, exception.getMessage());
            } else {
                log.info("Response - Method: {}, Path: {}, Duration: {}ms, Result: {}", 
                        method, path, duration, resultStr);
            }
        }
    }
    
    private boolean isFilteredType(Class<?> clazz) {
        return clazz.getName().startsWith("jakarta.servlet") 
                || clazz.getName().startsWith("org.springframework");
    }
}
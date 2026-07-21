package com.iy.api.aspect;

import com.iy.api.common.ResultVO;
import com.iy.api.common.util.SensitiveUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Component
public class SensitiveAspect {

    @Around("execution(* com.iy.api.controller..*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        
        if (result instanceof ResultVO<?> resultVO) {
            Object data = resultVO.getData();
            if (data != null) {
                desensitizeData(data);
            }
        }
        
        return result;
    }

    private void desensitizeData(Object data) {
        if (data == null) {
            return;
        }
        
        if (data instanceof Collection<?> collection) {
            for (Object item : collection) {
                SensitiveUtils.desensitize(item);
            }
        } else {
            SensitiveUtils.desensitize(data);
        }
    }
}
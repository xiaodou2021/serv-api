package com.iy.api.handler;

import com.iy.api.common.ResultHelper;
import com.iy.api.common.ResultVO;
import com.iy.api.common.constants.SystemConstants;
import com.iy.api.common.enums.BizCodeEnum;
import com.iy.api.common.exception.BizException;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultVO<Void> handleBizException(BizException e) {
        log.warn("Business exception: code={}, message={}", e.getCode(), e.getMessage());
        return ResultHelper.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Void> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation exception: {}", errorMessage);
        return ResultHelper.error(BizCodeEnum.PARAM_INVALID.getCode(), errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: {}", e.getMessage());
        return ResultHelper.error(BizCodeEnum.PARAM_INVALID.getCode(), e.getMessage());
    }

    @ExceptionHandler(CallNotPermittedException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResultVO<Void> handleCircuitBreakerException(CallNotPermittedException e) {
        log.error("Circuit breaker is open, service unavailable: {}", e.getMessage());
        return ResultHelper.error(BizCodeEnum.SERVICE_UNAVAILABLE.getCode(), BizCodeEnum.SERVICE_UNAVAILABLE.getMessage());
    }

    @ExceptionHandler(BulkheadFullException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResultVO<Void> handleBulkheadException(BulkheadFullException e) {
        log.error("Bulkhead full, too many concurrent requests: {}", e.getMessage());
        return ResultHelper.error(BizCodeEnum.RATE_LIMIT_EXCEEDED.getCode(), BizCodeEnum.RATE_LIMIT_EXCEEDED.getMessage());
    }

    @ExceptionHandler(RequestNotPermitted.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResultVO<Void> handleRateLimitException(RequestNotPermitted e) {
        log.error("Rate limit exceeded: {}", e.getMessage());
        return ResultHelper.error(BizCodeEnum.RATE_LIMIT_EXCEEDED.getCode(), BizCodeEnum.RATE_LIMIT_EXCEEDED.getMessage());
    }

    @ExceptionHandler(TimeoutException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ResultVO<Void> handleTimeoutException(TimeoutException e) {
        log.error("Request timeout: {}", e.getMessage());
        return ResultHelper.error(BizCodeEnum.SERVICE_UNAVAILABLE.getCode(), "请求超时，请稍后重试");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO<Void> handleException(Exception e) {
        log.error("Unexpected exception occurred", e);
        return ResultHelper.error(BizCodeEnum.SYSTEM_ERROR.getCode(), BizCodeEnum.SYSTEM_ERROR.getMessage());
    }
}

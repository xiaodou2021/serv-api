package com.iy.api.handler;

import com.iy.api.common.ResultHelper;
import com.iy.api.common.ResultVO;
import com.iy.api.common.constants.SystemConstants;
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

    @ExceptionHandler(CallNotPermittedException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResultVO<Void> handleCircuitBreakerException(CallNotPermittedException e) {
        log.error("Circuit breaker is open, service unavailable: {}", e.getMessage());
        return ResultHelper.error(SystemConstants.SERVER_ERROR_CODE, "Service temporarily unavailable, please try again later");
    }

    @ExceptionHandler(BulkheadFullException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResultVO<Void> handleBulkheadException(BulkheadFullException e) {
        log.error("Bulkhead full, too many concurrent requests: {}", e.getMessage());
        return ResultHelper.error(SystemConstants.CLIENT_ERROR_CODE, "Too many concurrent requests, please try again later");
    }

    @ExceptionHandler(RequestNotPermitted.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResultVO<Void> handleRateLimitException(RequestNotPermitted e) {
        log.error("Rate limit exceeded: {}", e.getMessage());
        return ResultHelper.error(SystemConstants.CLIENT_ERROR_CODE, "Rate limit exceeded, please try again later");
    }

    @ExceptionHandler(TimeoutException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ResultVO<Void> handleTimeoutException(TimeoutException e) {
        log.error("Request timeout: {}", e.getMessage());
        return ResultHelper.error(SystemConstants.SERVER_ERROR_CODE, "Request timeout, please try again later");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO<Void> handleException(Exception e) {
        log.error("Unexpected exception occurred", e);
        return ResultHelper.error(SystemConstants.SERVER_ERROR_CODE, "Internal server error");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Void> handleRuntimeException(RuntimeException e) {
        log.error("Runtime exception occurred", e);
        return ResultHelper.error(SystemConstants.CLIENT_ERROR_CODE, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Void> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("Validation exception occurred: {}", errorMessage);
        return ResultHelper.error(SystemConstants.CLIENT_ERROR_CODE, errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Illegal argument exception occurred", e);
        return ResultHelper.error(SystemConstants.CLIENT_ERROR_CODE, e.getMessage());
    }
}
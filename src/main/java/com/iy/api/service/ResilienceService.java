package com.iy.api.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 服务熔断器、限流、并发控制等测试类
 */
@Service
public class ResilienceService {

    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    @TimeLimiter(name = "backendA")
    @Bulkhead(name = "backendA")
    @RateLimiter(name = "backendA")
    public CompletableFuture<String> protectedService(String input) {
        return CompletableFuture.supplyAsync(() -> {
            if ("error".equals(input)) {
                throw new RuntimeException("Service failure");
            }
            return "Success: " + input;
        });
    }

    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    @TimeLimiter(name = "backendA")
    @Bulkhead(name = "backendA")
    @RateLimiter(name = "backendA")
    public String syncProtectedService(String input) {
        if ("error".equals(input)) {
            throw new RuntimeException("Service failure");
        }
        return "Success: " + input;
    }

    private CompletableFuture<String> fallback(String input, Throwable throwable) {
        return CompletableFuture.completedFuture("Fallback response for: " + input + ", Error: " + throwable.getMessage());
    }

    private String syncFallback(String input, Throwable throwable) {
        return "Fallback response for: " + input + ", Error: " + throwable.getMessage();
    }
}
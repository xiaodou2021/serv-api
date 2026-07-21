package com.iy.api.controller;

import com.iy.api.common.ResultHelper;
import com.iy.api.common.ResultVO;
import com.iy.api.service.ResilienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class ResilienceController {

    @Autowired
    private ResilienceService resilienceService;

    @GetMapping("/resilience/async")
    public CompletableFuture<ResultVO<String>> testAsyncResilience(@RequestParam(defaultValue = "test") String input) {
        return resilienceService.protectedService(input)
                .thenApply(result -> ResultHelper.success(result));
    }

    @GetMapping("/resilience/sync")
    public ResultVO<String> testSyncResilience(@RequestParam(defaultValue = "test") String input) {
        String result = resilienceService.syncProtectedService(input);
        return ResultHelper.success(result);
    }
}
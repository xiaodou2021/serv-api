package com.iy.api.controller;

import com.iy.api.common.ResultHelper;
import com.iy.api.common.ResultVO;
import com.iy.api.common.annotation.AuditLog;
import com.iy.api.common.annotation.RateLimit;
import com.iy.api.common.constants.CacheConstants;
import com.iy.api.common.enums.BizCodeEnum;
import com.iy.api.common.enums.OperationType;
import com.iy.api.common.enums.RateLimitType;
import com.iy.api.common.exception.BizException;
import com.iy.api.common.util.IdGenUtils;
import com.iy.api.common.utils.JwtTokenUtil;
import com.iy.api.model.entity.SysLoginUserEntity;
import com.iy.api.model.vo.UserVO;
import com.iy.api.service.impl.SysLoginUserServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysLoginUserServiceImpl sysLoginUserService;

    @Data
    public static class LoginRequest {
        private String account;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String account;
        private String password;
        private String nickname;
        private String phone;
        private String email;
    }

    @Data
    public static class LoginResponse {
        private String token;
        private String tokenType = "Bearer";
        private Long expiresIn;
        private UserVO user;
    }

    @PostMapping("/login")
    @RateLimit(maxRequests = 5, windowSeconds = 60, type = RateLimitType.IP, message = "登录请求过于频繁，请稍后再试")
    @AuditLog(module = "认证", type = OperationType.LOGIN, description = "用户登录")
    public ResultVO<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SysLoginUserEntity user = sysLoginUserService.getByAccount(request.getAccount());
        if (user == null) {
            throw new BizException(BizCodeEnum.ACCOUNT_NOT_FOUND);
        }

        String token = jwtTokenUtil.generateToken(String.valueOf(user.getUserId()), user.getAccount());
        String cacheKey = CacheConstants.LOGIN_TOKEN_KEY + user.getUserId();
        redisTemplate.opsForValue().set(cacheKey, user, jwtTokenUtil.getExpireTime(), TimeUnit.MILLISECONDS);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpiresIn(jwtTokenUtil.getExpireTime() / 1000);
        response.setUser(user.toUserVO());

        return ResultHelper.success(response);
    }

    @PostMapping("/register")
    @AuditLog(module = "认证", type = OperationType.REGISTER, description = "用户注册")
    public ResultVO<UserVO> register(@RequestBody RegisterRequest request) {
        SysLoginUserEntity existingUser = sysLoginUserService.getByAccount(request.getAccount());
        if (existingUser != null) {
            throw new BizException(BizCodeEnum.ACCOUNT_EXISTS);
        }

        SysLoginUserEntity user = new SysLoginUserEntity();
        user.setUserId(Long.valueOf(IdGenUtils.numericIdStr(8)));
        user.setAccount(request.getAccount());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setLockFlag(0);
        user.setDeleted(0);

        sysLoginUserService.save(user);
        return ResultHelper.success(user.toUserVO());
    }

    @PostMapping("/logout")
    @AuditLog(module = "认证", type = OperationType.LOGOUT, description = "用户登出")
    public ResultVO<Void> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof com.iy.api.model.security.LoginUser loginUser) {
            String userId = String.valueOf(loginUser.getUser().getUserId());
            String cacheKey = CacheConstants.LOGIN_TOKEN_KEY + userId;
            redisTemplate.delete(cacheKey);
        }
        SecurityContextHolder.clearContext();
        return ResultHelper.success(null);
    }

    @GetMapping("/me")
    public ResultVO<SysLoginUserEntity> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof com.iy.api.model.security.LoginUser loginUser) {
            return ResultHelper.success(loginUser.getUser());
        }
        throw new BizException(BizCodeEnum.UNAUTHORIZED);
    }
}

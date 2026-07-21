package com.iy.api.controller;

import com.iy.api.common.ResultHelper;
import com.iy.api.common.ResultVO;
import com.iy.api.common.constants.CacheConstants;
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
        private SysLoginUserEntity user;
    }

    @PostMapping("/login")
    public ResultVO<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SysLoginUserEntity user = sysLoginUserService.getByAccount(request.getAccount());
        if (user == null) {
            return ResultHelper.error("401", "User not found");
        }

        String token = jwtTokenUtil.generateToken(String.valueOf(user.getUserId()), user.getAccount());
        String cacheKey = CacheConstants.LOGIN_TOKEN_KEY + user.getUserId();
        redisTemplate.opsForValue().set(cacheKey, user, jwtTokenUtil.getExpireTime(), TimeUnit.MILLISECONDS);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpiresIn(jwtTokenUtil.getExpireTime() / 1000);
        response.setUser(user);

        return ResultHelper.success(response);
    }

    @PostMapping("/register")
    public ResultVO<UserVO> register(@RequestBody RegisterRequest request) {
        SysLoginUserEntity existingUser = sysLoginUserService.getByAccount(request.getAccount());
        if (existingUser != null) {
            return ResultHelper.error("400", "Account already exists");
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
        return ResultHelper.error("401", "Unauthorized");
    }
}
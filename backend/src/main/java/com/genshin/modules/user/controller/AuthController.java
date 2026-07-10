package com.genshin.modules.user.controller;

import com.genshin.aop.OperationLog;
import com.genshin.common.Result;
import com.genshin.modules.user.dto.AuthResponse;
import com.genshin.modules.user.dto.LoginRequest;
import com.genshin.modules.user.service.AuthService;
import com.genshin.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @OperationLog(action = "注册", resource = "用户")
    public Result<AuthResponse> register(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.register(request));
    }

    @PostMapping("/login")
    @OperationLog(action = "登录", resource = "用户")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @PostMapping("/refresh")
    public Result<AuthResponse> refresh(@RequestBody Map<String, String> body) {
        String token = body.get("refreshToken");
        if (token == null || token.isBlank()) {
            return Result.error("refreshToken 不能为空");
        }
        return Result.success(authService.refresh(token));
    }

    @GetMapping("/me")
    public Result<AuthResponse.UserInfo> me(@AuthenticationPrincipal AuthUser user) {
        return Result.success(authService.getUserInfo(user.getUserId()));
    }
}

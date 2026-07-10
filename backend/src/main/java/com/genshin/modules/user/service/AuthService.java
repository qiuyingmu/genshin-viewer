package com.genshin.modules.user.service;

import com.genshin.common.BusinessException;
import com.genshin.modules.user.dto.AuthResponse;
import com.genshin.modules.user.dto.LoginRequest;
import com.genshin.modules.user.entity.SysUser;
import com.genshin.modules.user.repository.UserRepository;
import com.genshin.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(LoginRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw BusinessException.conflict("用户名已存在");
        }

        var user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getUsername());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.insert(user);

        return generateAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw BusinessException.unauthorized("用户名或密码错误");
        }

        SysUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> BusinessException.notFound("用户", request.getUsername()));

        return generateAuthResponse(user);
    }

    public AuthResponse refresh(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw BusinessException.unauthorized("无效的刷新令牌");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        SysUser user = userRepository.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户", userId);
        }

        return generateAuthResponse(user);
    }

    public AuthResponse.UserInfo getUserInfo(Long userId) {
        SysUser user = userRepository.selectById(userId);
        if (user == null) throw BusinessException.notFound("用户", userId);
        return new AuthResponse.UserInfo(user.getId(), user.getUsername(), user.getNickname());
    }

    private AuthResponse generateAuthResponse(SysUser user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        var userInfo = new AuthResponse.UserInfo(user.getId(), user.getUsername(), user.getNickname());
        return new AuthResponse(accessToken, refreshToken, "Bearer", userInfo);
    }
}

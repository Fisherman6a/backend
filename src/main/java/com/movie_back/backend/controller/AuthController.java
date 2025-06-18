package com.movie_back.backend.controller;

import com.movie_back.backend.dto.auth.AuthRequest;
import com.movie_back.backend.dto.auth.AuthResponse;
import com.movie_back.backend.dto.user.UserDTO;
import com.movie_back.backend.dto.user.UserRegistrationRequest;
import com.movie_back.backend.entity.User;
import com.movie_back.backend.security.JwtUtil;
import com.movie_back.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 用户登录接口
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        // 使用 AuthenticationManager 进行用户认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        // 认证成功后，获取用户信息
        User user = (User) authentication.getPrincipal();
        // 生成JWT
        String token = jwtUtil.generateToken(user);

        // 返回JWT和一些用户信息
        AuthResponse response = new AuthResponse(token, user.getId(), user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(response);
    }

    // 用户注册接口
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegistrationRequest request) {
        UserDTO newUser = userService.registerUser(request);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
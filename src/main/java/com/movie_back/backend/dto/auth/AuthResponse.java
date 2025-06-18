package com.movie_back.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

// 登录成功后返回的数据传输对象
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private String role;
}
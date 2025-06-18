package com.movie_back.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 登录请求的数据传输对象
@Data
public class AuthRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
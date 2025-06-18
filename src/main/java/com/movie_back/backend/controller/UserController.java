package com.movie_back.backend.controller;

import com.movie_back.backend.dto.user.UserDTO;
import com.movie_back.backend.entity.User;
import com.movie_back.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 获取当前登录用户的信息
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // 确保用户已登录
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        // @AuthenticationPrincipal可以直接注入当前登录的User对象
        UserDTO userDTO = userService.findUserById(currentUser.getId());
        return ResponseEntity.ok(userDTO);
    }

    // 根据ID获取用户信息 - 仅限管理员
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.findUserById(id);
        return ResponseEntity.ok(userDTO);
    }
}
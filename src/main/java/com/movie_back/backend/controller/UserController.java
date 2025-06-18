package com.movie_back.backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable("id") Long id) {
        return "根据ID获取用户信息";
    }

    @PostMapping("/user")
    public String create() {
        return "创建用户";
    }

    @PutMapping("/user")
    public String update() {
        return "更新用户";
    }

    @DeleteMapping("/user/{id}")
    public String delete(@PathVariable("id") Long id) {
        return "根据ID删除用户";
    }
}

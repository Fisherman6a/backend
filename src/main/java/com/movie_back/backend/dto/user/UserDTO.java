package com.movie_back.backend.dto.user;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
}
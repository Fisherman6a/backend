package com.movie_back.backend.dto.review;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewRequest { // 用于创建和更新
    @NotBlank
    private String commentText;
}
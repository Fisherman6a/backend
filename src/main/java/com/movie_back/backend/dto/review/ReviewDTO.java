package com.movie_back.backend.dto.review;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO { // 用于展示
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long userId;
    private String username;
    private String commentText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
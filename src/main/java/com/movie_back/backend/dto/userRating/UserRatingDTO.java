package com.movie_back.backend.dto.userRating;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserRatingDTO { // 用于展示
    private Long id;
    private Long movieId;
    private Long userId;
    private Integer score;
    private LocalDateTime ratedAt;
}

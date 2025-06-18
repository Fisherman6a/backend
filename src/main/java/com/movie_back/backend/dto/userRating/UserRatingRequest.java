package com.movie_back.backend.dto.userRating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRatingRequest { // 用于创建和更新
    @NotNull
    @Min(1)
    @Max(10) // 假设1-10分制
    private Integer score;
}
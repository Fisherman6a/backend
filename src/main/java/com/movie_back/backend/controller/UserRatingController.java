package com.movie_back.backend.controller;

import com.movie_back.backend.dto.userRating.UserRatingDTO;
import com.movie_back.backend.dto.userRating.UserRatingRequest;
import com.movie_back.backend.service.UserRatingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies/{movieId}/ratings")
public class UserRatingController {

    private final UserRatingService userRatingService;

    public UserRatingController(UserRatingService userRatingService) {
        this.userRatingService = userRatingService;
    }

    /**
     * 用户为电影添加或更新评分的端点。
     * 为简化起见，userId 作为请求参数传递。在真实应用中，
     * 这应该从已认证用户的安全主体中获取。
     *
     * @param movieId 待评分的电影 ID。
     * @param userId  评分用户的 ID。
     * @param request 评分分数。
     * @return 创建或更新后评分的 DTO。
     */
    @PostMapping
    public ResponseEntity<UserRatingDTO> addOrUpdateRating(
            @PathVariable Long movieId,
            @RequestParam Long userId, // 暂时简化处理
            @Valid @RequestBody UserRatingRequest request) {
        UserRatingDTO ratingDTO = userRatingService.addOrUpdateRating(movieId, userId, request);
        return ResponseEntity.ok(ratingDTO);
    }
}
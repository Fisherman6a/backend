package com.movie_back.backend.controller;

import com.movie_back.backend.dto.review.ReviewDTO;
import com.movie_back.backend.dto.review.ReviewRequest;
import com.movie_back.backend.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 为电影添加评论的端点。
     * 为简化起见，userId 作为请求参数传递。在真实世界的应用中，
     * 这通常应从安全上下文（例如，已登录用户）中获取。
     *
     * @param movieId       要评论的电影 ID。
     * @param userId        提交评论的用户 ID。
     * @param reviewRequest 评论的内容。
     * @return 创建的评论。
     */
    @PostMapping("/movies/{movieId}/reviews")
    public ResponseEntity<ReviewDTO> addReviewToMovie(
            @PathVariable Long movieId,
            @RequestParam Long userId, // 简化处理：在真实应用中，从安全上下文中获取
            @Valid @RequestBody ReviewRequest reviewRequest) {
        ReviewDTO createdReview = reviewService.addReview(movieId, userId, reviewRequest);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    /**
     * 获取特定电影所有评论的端点。
     * 
     * @param movieId 电影的 ID。
     * @return 评论列表。
     */
    @GetMapping("/movies/{movieId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviewsForMovie(@PathVariable Long movieId) {
        List<ReviewDTO> reviews = reviewService.getReviewsForMovie(movieId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * 删除评论的端点。
     * 
     * @param reviewId 要删除的评论 ID。
     * @return 成功删除后返回无内容的响应。
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
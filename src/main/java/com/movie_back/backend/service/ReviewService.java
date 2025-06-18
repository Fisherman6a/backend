package com.movie_back.backend.service;

import com.movie_back.backend.dto.review.ReviewDTO;
import com.movie_back.backend.dto.review.ReviewRequest;
import com.movie_back.backend.entity.Movie;
import com.movie_back.backend.entity.Review;
import com.movie_back.backend.entity.User;
import com.movie_back.backend.exception.ResourceNotFoundException;
import com.movie_back.backend.repository.MovieRepository;
import com.movie_back.backend.repository.ReviewRepository;
import com.movie_back.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, MovieRepository movieRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    /**
     * 获取特定电影的所有评论。
     * 
     * @param movieId 电影的 ID。
     * @return 评论 DTO 的列表。
     */
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsForMovie(Long movieId) {
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        return reviews.stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
    }

    /**
     * 为电影添加一条新评论。
     * 
     * @param movieId       被评论的电影 ID。
     * @param userId        发表评论的用户 ID。
     * @param reviewRequest 评论内容。
     * @return 已创建评论的 DTO。
     */
    @Transactional
    public ReviewDTO addReview(Long movieId, Long userId, ReviewRequest reviewRequest) {
        // 从数据库中查找电影和用户实体。
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到 ID 为 " + movieId + " 的电影"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到 ID 为 " + userId + " 的用户"));

        Review review = new Review();
        review.setMovie(movie);
        review.setUser(user);
        review.setCommentText(reviewRequest.getCommentText());

        Review savedReview = reviewRepository.save(review);
        return convertToReviewDTO(savedReview);
    }

    /**
     * 根据 ID 删除一条评论。
     * 
     * @param reviewId 要删除的评论 ID。
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("未找到 ID 为 " + reviewId + " 的评论");
        }
        reviewRepository.deleteById(reviewId);
    }

    /**
     * 将 Review 实体转换为其 DTO 表现形式。
     * 
     * @param review Review 实体。
     * @return 对应的 ReviewDTO。
     */
    private ReviewDTO convertToReviewDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setCommentText(review.getCommentText());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        dto.setMovieId(review.getMovie().getId());
        dto.setMovieTitle(review.getMovie().getTitle());
        dto.setUserId(review.getUser().getId());
        dto.setUsername(review.getUser().getUsername());
        return dto;
    }
}
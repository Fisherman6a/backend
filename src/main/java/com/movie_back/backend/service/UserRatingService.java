package com.movie_back.backend.service;

import com.movie_back.backend.dto.userRating.UserRatingDTO;
import com.movie_back.backend.dto.userRating.UserRatingRequest;
import com.movie_back.backend.entity.Movie;
import com.movie_back.backend.entity.User;
import com.movie_back.backend.entity.UserRating;
import com.movie_back.backend.exception.ResourceNotFoundException;
import com.movie_back.backend.repository.MovieRepository;
import com.movie_back.backend.repository.UserRepository;
import com.movie_back.backend.repository.UserRatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserRatingService {

    private final UserRatingRepository userRatingRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final MovieService movieService; // 用于触发平均分更新

    public UserRatingService(UserRatingRepository userRatingRepository, MovieRepository movieRepository,
            UserRepository userRepository, MovieService movieService) {
        this.userRatingRepository = userRatingRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.movieService = movieService;
    }

    /**
     * 添加或更新用户对电影的评分。
     * 如果用户已经对该电影评分，则更新现有评分。
     * 否则，创建一条新评分。
     *
     * @param movieId 电影的 ID。
     * @param userId  用户的 ID。
     * @param request 评分数据。
     * @return 创建或更新后评分的 DTO。
     */
    @Transactional
    public UserRatingDTO addOrUpdateRating(Long movieId, Long userId, UserRatingRequest request) {
        // 确保电影和用户存在
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到 ID 为 " + movieId + " 的电影"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到 ID 为 " + userId + " 的用户"));

        // 检查该用户是否已对该电影评分
        Optional<UserRating> existingRatingOpt = userRatingRepository.findByMovieIdAndUserId(movieId, userId);

        UserRating userRating;
        if (existingRatingOpt.isPresent()) {
            // 更新现有评分
            userRating = existingRatingOpt.get();
            userRating.setScore(request.getScore());
        } else {
            // 创建新评分
            userRating = new UserRating();
            userRating.setMovie(movie);
            userRating.setUser(user);
            userRating.setScore(request.getScore());
        }

        UserRating savedRating = userRatingRepository.save(userRating);

        // 保存评分后，触发电影平均分的更新。
        movieService.updateMovieAverageRating(movieId);

        return convertToUserRatingDTO(savedRating);
    }

    /**
     * 将 UserRating 实体转换为其 DTO 表现形式。
     * 
     * @param rating UserRating 实体。
     * @return 对应的 UserRatingDTO。
     */
    private UserRatingDTO convertToUserRatingDTO(UserRating rating) {
        UserRatingDTO dto = new UserRatingDTO();
        dto.setId(rating.getId());
        dto.setScore(rating.getScore());
        dto.setRatedAt(rating.getRatedAt());
        dto.setMovieId(rating.getMovie().getId());
        dto.setUserId(rating.getUser().getId());
        return dto;
    }
}
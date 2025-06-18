package com.movie_back.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.movie_back.backend.dto.movie.CreateMovieRequest;
import com.movie_back.backend.dto.movie.MovieDTO;
import com.movie_back.backend.entity.*;
import com.movie_back.backend.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;

    public MovieService(MovieRepository movieRepository, ActorRepository actorRepository,
            DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
    }

    @Transactional
    public MovieDTO createMovie(CreateMovieRequest request) {
        Movie movie = new Movie();
        // ... 设置基本属性从 request 到 movie ...
        movie.setTitle(request.getTitle());
        movie.setReleaseYear(request.getReleaseYear());
        // ... etc.

        if (request.getActorIds() != null && !request.getActorIds().isEmpty()) {
            Set<Actor> actors = new HashSet<>(actorRepository.findAllById(request.getActorIds()));
            movie.setCast(actors);
        }
        if (request.getDirectorIds() != null && !request.getDirectorIds().isEmpty()) {
            Set<Director> directors = new HashSet<>(directorRepository.findAllById(request.getDirectorIds()));
            movie.setDirectors(directors);
        }
        Movie savedMovie = movieRepository.save(movie);
        return convertToMovieDTO(savedMovie);
    }

    @Transactional
    public void updateMovieAverageRating(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found")); // 自定义异常

        // 从 UserRatingRepository 获取该电影所有评分并计算平均值
        // 这是一个简化示例，实际中 UserRatingRepository 会提供一个计算平均分的方法
        double average = movie.getUserRatings().stream()
                .mapToInt(ur -> ur.getScore())
                .average()
                .orElse(0.0);
        // 保留两位小数
        movie.setAverageRating(Math.round(average * 100.0) / 100.0);
        movieRepository.save(movie);
    }

    // page为当前页号，size为每页包含的电影数量
    public Page<MovieDTO> searchMovies(Integer releaseYear, String genre, String country, Double minRating,
            String sortBy,
            String sortDir, int page, int size) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Specification接口在 JPA 中是用来构建动态查询条件的工具
        Specification<Movie> spec = Specification.where(null);

        // releaseYear、genre、minRating三个条件为可选，三者只要满足非空就能成为spec的筛选条件，三者之间是与关系
        if (releaseYear != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("releaseYear"), releaseYear));
        }
        // 根据电影类型进行筛选
        if (genre != null && !genre.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("genre")), "%" + genre.toLowerCase() + "%"));
        }
        // 国家地区筛选
        if (country != null && !country.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("region")),
                    country.toLowerCase()));
        }
        // 设定minRating最低评分门槛，筛出比这个分高的电影
        if (minRating != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("averageRating"), minRating));
        }

        Page<Movie> moviePage = movieRepository.findAll(spec, pageable);
        return moviePage.map(this::convertToMovieDTO);
    }

    public List<MovieDTO> getHotMovies(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "averageRating")); // 简单定义为评分最高
        return movieRepository.findTopRatedMovies(pageable).stream()
                .map(this::convertToMovieDTO)
                .collect(Collectors.toList());
    }

    // ... 其他 CRUD 方法 ...

    private MovieDTO convertToMovieDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setDuration(movie.getDuration());
        dto.setGenre(movie.getGenre());
        dto.setLanguage(movie.getLanguage());
        dto.setCountry(movie.getCountry());
        dto.setSynopsis(movie.getSynopsis());
        dto.setAverageRating(movie.getAverageRating());
        dto.setPosterUrl(movie.getPosterUrl());
        if (movie.getCast() != null) {
            dto.setActorNames(movie.getCast().stream().map(Actor::getName).collect(Collectors.toSet()));
            dto.setActorIds(movie.getCast().stream().map(Actor::getId).collect(Collectors.toSet()));
        }
        if (movie.getDirectors() != null) {
            dto.setDirectorNames(movie.getDirectors().stream().map(Director::getName).collect(Collectors.toSet()));
            dto.setDirectorIds(movie.getDirectors().stream().map(Director::getId).collect(Collectors.toSet()));
        }
        return dto;
    }
}
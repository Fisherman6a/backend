package com.movie_back.backend.service;

import com.movie_back.backend.dto.movie.CreateMovieRequest;
import com.movie_back.backend.dto.movie.MovieDTO;
import com.movie_back.backend.entity.Actor;
import com.movie_back.backend.entity.Director;
import com.movie_back.backend.entity.Movie;
import com.movie_back.backend.entity.UserRating;
import com.movie_back.backend.exception.ResourceNotFoundException;
import com.movie_back.backend.repository.ActorRepository;
import com.movie_back.backend.repository.DirectorRepository;
import com.movie_back.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 使用 Lombok 自动注入 final 字段
public class MovieService {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;

    @Transactional
    public MovieDTO createMovie(CreateMovieRequest request) {
        Movie movie = new Movie();
        // 设置基本属性
        setMoviePropertiesFromRequest(movie, request);
        Movie savedMovie = movieRepository.save(movie);
        return convertToMovieDTO(savedMovie);
    }

    // 根据ID获取电影
    @Transactional(readOnly = true)
    public MovieDTO getMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));
        return convertToMovieDTO(movie);
    }

    // 更新电影信息
    @Transactional
    public MovieDTO updateMovie(Long movieId, CreateMovieRequest request) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));

        // 更新属性
        setMoviePropertiesFromRequest(movie, request);
        Movie updatedMovie = movieRepository.save(movie);
        return convertToMovieDTO(updatedMovie);
    }

    // 删除电影
    @Transactional
    public void deleteMovie(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found with id: " + movieId);
        }
        movieRepository.deleteById(movieId);
    }

    @Transactional
    public void updateMovieAverageRating(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));

        // 从 UserRating 集合中计算平均分
        double average = movie.getUserRatings().stream()
                .mapToInt(UserRating::getScore)
                .average()
                .orElse(0.0);

        // 保留一位小数
        movie.setAverageRating(Math.round(average * 10.0) / 10.0);
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

        if (releaseYear != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("releaseYear"), releaseYear));
        }
        if (genre != null && !genre.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("genre")), "%" + genre.toLowerCase() + "%"));
        }
        if (country != null && !country.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("country")), country.toLowerCase()));
        }
        if (minRating != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("averageRating"), minRating));
        }

        Page<Movie> moviePage = movieRepository.findAll(spec, pageable);
        return moviePage.map(this::convertToMovieDTO);
    }

    public List<MovieDTO> getHotMovies(int limit) {
        // 按平均分降序，取前N个
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "averageRating"));
        return movieRepository.findAll(pageable).stream()
                .map(this::convertToMovieDTO)
                .collect(Collectors.toList());
    }

    // 抽取的公共方法，用于从请求设置电影属性
    private void setMoviePropertiesFromRequest(Movie movie, CreateMovieRequest request) {
        movie.setTitle(request.getTitle());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setDuration(request.getDuration());
        movie.setGenre(request.getGenre());
        movie.setLanguage(request.getLanguage());
        movie.setCountry(request.getCountry());
        movie.setSynopsis(request.getSynopsis());
        movie.setPosterUrl(request.getPosterUrl());

        if (request.getActorIds() != null && !request.getActorIds().isEmpty()) {
            List<Actor> actors = actorRepository.findAllById(request.getActorIds());
            movie.setCast(new HashSet<>(actors));
        } else {
            movie.getCast().clear();
        }

        if (request.getDirectorIds() != null && !request.getDirectorIds().isEmpty()) {
            List<Director> directors = directorRepository.findAllById(request.getDirectorIds());
            movie.setDirectors(new HashSet<>(directors));
        } else {
            movie.getDirectors().clear();
        }
    }

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
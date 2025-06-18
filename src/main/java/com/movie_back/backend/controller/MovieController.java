package com.movie_back.backend.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.movie_back.backend.dto.movie.CreateMovieRequest;
import com.movie_back.backend.dto.movie.MovieDTO;
import com.movie_back.backend.service.MovieService;

// ... 其他导入 ...
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody CreateMovieRequest movieRequest) {
        // 在实际应用中，部分操作可能需要管理员权限，这里简化
        return new ResponseEntity<>(movieService.createMovie(movieRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        // MovieDTO movie = movieService.getMovieById(id); // 假设MovieService有此方法返回DTO
        // return ResponseEntity.ok(movie);
        return ResponseEntity.ok().build(); // 示例，需完整实现
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MovieDTO>> searchMovies(
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MovieDTO> movies = movieService.searchMovies(releaseYear, genre, country, minRating, sortBy, sortDir, page,
                size);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/hot")
    public ResponseEntity<List<MovieDTO>> getHotMovies(@RequestParam(defaultValue = "5") int limit) {
        List<MovieDTO> hotMovies = movieService.getHotMovies(limit);
        return ResponseEntity.ok(hotMovies);
    }
    // ... ...
}
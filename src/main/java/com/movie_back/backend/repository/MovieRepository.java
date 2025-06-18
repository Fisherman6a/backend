package com.movie_back.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // 用于复杂查询
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.movie_back.backend.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    // 可以按照电影的发行年月、类型/流派、评分等信息进行组合查询和排序 (使用Specification API)

    // 示例：按类型查找
    List<Movie> findByGenreContainingIgnoreCase(String genre);

    // 示例：查找某演员参演的电影
    @Query("SELECT m FROM Movie m JOIN m.cast a WHERE a.id = :actorId")
    List<Movie> findMoviesByActorId(@Param("actorId") Long actorId);

    // 示例：查找某导演执导的电影
    @Query("SELECT m FROM Movie m JOIN m.directors d WHERE d.id = :directorId")
    List<Movie> findMoviesByDirectorId(@Param("directorId") Long directorId);

    // 热门电影推荐 (示例：按平均分降序，取前N个)
    @Query("SELECT m FROM Movie m ORDER BY m.averageRating DESC")
    List<Movie> findTopRatedMovies(org.springframework.data.domain.Pageable pageable);
}
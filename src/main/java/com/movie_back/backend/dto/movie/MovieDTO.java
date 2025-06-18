package com.movie_back.backend.dto.movie;

import lombok.Data;
import java.util.Set;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private Integer releaseYear;
    private Integer duration;
    private String genre;
    private String language;
    private String country;
    private String synopsis;
    private Double averageRating;
    private String posterUrl;
    private Set<Long> actorIds; // 用于创建/更新时关联演员
    private Set<String> actorNames; // 用于展示
    private Set<Long> directorIds; // 用于创建/更新时关联导演
    private Set<String> directorNames; // 用于展示
}
package com.movie_back.backend.dto.movie;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Set;

@Data
public class CreateMovieRequest {
    @NotBlank
    private String title;
    @NotNull
    private Integer releaseYear;
    private Integer duration;
    private String genre;
    private String language;
    private String country;
    private String synopsis;
    private String posterUrl;
    private Set<Long> actorIds;
    private Set<Long> directorIds;
}
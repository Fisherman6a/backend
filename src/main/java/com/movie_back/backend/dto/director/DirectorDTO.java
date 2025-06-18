package com.movie_back.backend.dto.director;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DirectorDTO {
    private Long id;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String nationality;
    private String profileImageUrl;
}
package com.movie_back.backend.dto.actor;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ActorDTO {
    private Long id;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String nationality;
    private String profileImageUrl;
}
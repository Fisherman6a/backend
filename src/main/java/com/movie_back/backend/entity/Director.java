package com.movie_back.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "directors")
@Data
@EqualsAndHashCode(exclude = "moviesDirected")
@ToString(exclude = "moviesDirected")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // DirectorID

    @Column(nullable = false)
    private String name; // 姓名

    private String gender; // 性别

    private LocalDate birthDate; // 出生日期

    private String nationality; // 国籍

    private String profileImageUrl; // 导演图片URL

    @ManyToMany(mappedBy = "directors", fetch = FetchType.LAZY)
    private Set<Movie> moviesDirected = new HashSet<>();
}
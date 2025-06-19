package com.movie_back.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@Data
@EqualsAndHashCode(exclude = { "cast", "directors", "reviews", "userRatings" }) // 避免循环引用导致的hashCode和equals问题
@ToString(exclude = { "cast", "directors", "reviews", "userRatings" }) // 避免循环引用导致的toString问题
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // MovieID

    @Column(nullable = false)
    private String title; // 电影名称

    private Integer releaseYear; // 发行年份

    private Integer duration; // 电影时长(min)

    private String genre; // 类型/流派

    private String language; // 语言

    private String country; // 国家/地区

    @Lob
    private String synopsis; // 简介

    @Column
    private Double averageRating = 0.0; // 评分 (由UserRating计算得出)

    private String posterUrl; // 海报图片URL (用于图形化显示)

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "movie_actors", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> cast = new HashSet<>(); // 参演演员

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "movie_directors", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "director_id"))
    private Set<Director> directors = new HashSet<>(); // 导演

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserRating> userRatings = new HashSet<>();
}
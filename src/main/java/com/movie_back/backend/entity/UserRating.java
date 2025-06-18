package com.movie_back.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_ratings", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "movie_id" }) // 用户对同一电影只能评分一次
})
@Data
public class UserRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer score; // 例如 1-10 或 1-5

    private LocalDateTime ratedAt;

    @PrePersist
    protected void onRate() {
        ratedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdateRate() {
        ratedAt = LocalDateTime.now();
    }
}
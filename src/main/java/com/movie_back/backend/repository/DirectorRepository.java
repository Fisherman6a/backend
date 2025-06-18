package com.movie_back.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie_back.backend.entity.Director;

public interface DirectorRepository extends JpaRepository<Director, Long> {
}

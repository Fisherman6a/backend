package com.movie_back.backend.repository;

// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie_back.backend.entity.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    // List<Actor> findById(String id);
}
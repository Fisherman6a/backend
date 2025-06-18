package com.movie_back.backend.repository;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie_back.backend.entity.Director;

public interface DirectorRepository extends JpaRepository<Director, Long> {

    HashSet<Director> findAllById(Set<Long> directorIds);

}

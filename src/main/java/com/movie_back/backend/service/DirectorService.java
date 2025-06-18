package com.movie_back.backend.service;

import com.movie_back.backend.dto.director.DirectorDTO;
import com.movie_back.backend.entity.Director;
import com.movie_back.backend.exception.ResourceNotFoundException;
import com.movie_back.backend.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    @Transactional
    public DirectorDTO createDirector(DirectorDTO directorDTO) {
        Director director = new Director();
        director.setName(directorDTO.getName());
        director.setGender(directorDTO.getGender());
        director.setBirthDate(directorDTO.getBirthDate());
        director.setNationality(directorDTO.getNationality());
        director.setProfileImageUrl(directorDTO.getProfileImageUrl());
        Director savedDirector = directorRepository.save(director);
        return convertToDTO(savedDirector);
    }

    @Transactional(readOnly = true)
    public DirectorDTO getDirectorById(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director not found with id: " + id));
        return convertToDTO(director);
    }

    @Transactional(readOnly = true)
    public List<DirectorDTO> getAllDirectors() {
        return directorRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public DirectorDTO updateDirector(Long id, DirectorDTO directorDetails) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director not found with id: " + id));
        director.setName(directorDetails.getName());
        director.setGender(directorDetails.getGender());
        director.setBirthDate(directorDetails.getBirthDate());
        director.setNationality(directorDetails.getNationality());
        director.setProfileImageUrl(directorDetails.getProfileImageUrl());
        Director updatedDirector = directorRepository.save(director);
        return convertToDTO(updatedDirector);
    }

    @Transactional
    public void deleteDirector(Long id) {
        if (!directorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Director not found with id: " + id);
        }
        directorRepository.deleteById(id);
    }

    private DirectorDTO convertToDTO(Director director) {
        DirectorDTO dto = new DirectorDTO();
        dto.setId(director.getId());
        dto.setName(director.getName());
        dto.setGender(director.getGender());
        dto.setBirthDate(director.getBirthDate());
        dto.setNationality(director.getNationality());
        dto.setProfileImageUrl(director.getProfileImageUrl());
        return dto;
    }
}
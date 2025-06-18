package com.movie_back.backend.service;

import com.movie_back.backend.dto.actor.ActorDTO;
import com.movie_back.backend.entity.Actor;
import com.movie_back.backend.exception.ResourceNotFoundException;
import com.movie_back.backend.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    @Transactional
    public ActorDTO createActor(ActorDTO actorDTO) {
        Actor actor = new Actor();
        actor.setName(actorDTO.getName());
        actor.setGender(actorDTO.getGender());
        actor.setBirthDate(actorDTO.getBirthDate());
        actor.setNationality(actorDTO.getNationality());
        actor.setProfileImageUrl(actorDTO.getProfileImageUrl());
        Actor savedActor = actorRepository.save(actor);
        return convertToDTO(savedActor);
    }

    @Transactional(readOnly = true)
    public ActorDTO getActorById(Long id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
        return convertToDTO(actor);
    }

    @Transactional(readOnly = true)
    public List<ActorDTO> getAllActors() {
        return actorRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public ActorDTO updateActor(Long id, ActorDTO actorDetails) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));

        actor.setName(actorDetails.getName());
        actor.setGender(actorDetails.getGender());
        actor.setBirthDate(actorDetails.getBirthDate());
        actor.setNationality(actorDetails.getNationality());
        actor.setProfileImageUrl(actorDetails.getProfileImageUrl());

        Actor updatedActor = actorRepository.save(actor);
        return convertToDTO(updatedActor);
    }

    @Transactional
    public void deleteActor(Long id) {
        if (!actorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Actor not found with id: " + id);
        }
        actorRepository.deleteById(id);
    }

    private ActorDTO convertToDTO(Actor actor) {
        ActorDTO dto = new ActorDTO();
        dto.setId(actor.getId());
        dto.setName(actor.getName());
        dto.setGender(actor.getGender());
        dto.setBirthDate(actor.getBirthDate());
        dto.setNationality(actor.getNationality());
        dto.setProfileImageUrl(actor.getProfileImageUrl());
        return dto;
    }
}
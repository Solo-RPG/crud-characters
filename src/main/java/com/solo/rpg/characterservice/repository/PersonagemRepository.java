package com.solo.rpg.characterservice.repository;

import com.solo.rpg.characterservice.model.Personagem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonagemRepository extends MongoRepository<Personagem, String> {
    List<Personagem> findByOwnerId(String ownerId);
    Optional<Personagem> findByIdAndOwnerId(String id, String ownerId);
}
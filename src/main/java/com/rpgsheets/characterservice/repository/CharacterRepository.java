package com.rpgsheets.characterservice.repository;

import com.rpgsheets.characterservice.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {
    // Métodos de CRUD para Personagens são fornecidos automaticamente.
    // Você pode adicionar métodos personalizados aqui, como findByPlayerUserId.
}
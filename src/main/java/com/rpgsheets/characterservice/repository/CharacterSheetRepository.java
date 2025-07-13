package com.rpgsheets.characterservice.repository;

import com.rpgsheets.characterservice.model.CharacterSheet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// estende MongoRepository, que fornece métodos CRUD comuns
// automaticamente (como save, findById, findAll, deleteById).
// O primeiro tipo genérico é a classe do documento (CharacterSheet),
// o segundo é o tipo do ID do documento (String).
@Repository // Indica ao Spring que esta interface é um componente de repositório
public interface CharacterSheetRepository extends MongoRepository<CharacterSheet, String> {
    // Você pode adicionar métodos de consulta personalizados aqui se precisar
    // (ex: findByOwnerId(String ownerId))
}
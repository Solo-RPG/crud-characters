package com.solo.rpg.characterservice.repository;

import com.solo.rpg.characterservice.model.Personagem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Indica que esta é uma classe de repositório
public interface PersonagemRepository extends MongoRepository<Personagem, String> {
    // MongoRepository<TipoDaEntidade, TipoDoId>

    // Métodos de consulta personalizados (Spring Data magic!)
    List<Personagem> findByOwnerId(String ownerId);


    List<Personagem> findByTemplateSystemName(String templateSystemName);
}
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
    // Baseado no seu endpoint GET /api/sheets/by-user_id/{user_id}
    List<Personagem> findByOwnerId(String ownerId);

    // Você não tinha um "find by templateSystemName" direto no CRUD de fichas para buscar a ficha,
    // mas sim para buscar o TEMPLATE. Se você precisasse buscar personagens por nome do sistema,
    // adicionaria:
    List<Personagem> findByTemplateSystemName(String templateSystemName);
}
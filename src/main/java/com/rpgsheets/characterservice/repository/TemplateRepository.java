package com.rpgsheets.characterservice.repository;

import com.rpgsheets.characterservice.model.Template;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateRepository extends MongoRepository<Template, String> {
    // Método para encontrar um template pelo systemName
    Optional<Template> findBySystemName(String systemName);
}
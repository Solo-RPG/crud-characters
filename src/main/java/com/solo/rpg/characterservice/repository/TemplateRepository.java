package com.solo.rpg.characterservice.repository;

import com.solo.rpg.characterservice.model.template.TemplateDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TemplateRepository extends MongoRepository<TemplateDocument, String> {
    Optional<TemplateDocument> findBySystemName(String systemName);
}

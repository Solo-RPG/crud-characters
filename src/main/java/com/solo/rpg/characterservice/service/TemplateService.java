package com.solo.rpg.characterservice.service;

import com.solo.rpg.characterservice.exceptions.TemplateNotFoundException;
import com.solo.rpg.characterservice.model.template.TemplateCreate;
import com.solo.rpg.characterservice.model.template.TemplateDocument;
import com.solo.rpg.characterservice.model.template.TemplateField;
import com.solo.rpg.characterservice.model.template.TemplateResponse;
import com.solo.rpg.characterservice.repository.TemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Transactional
    public TemplateResponse createTemplate(TemplateCreate request) {
        TemplateDocument template = new TemplateDocument();
        template.setSystemName(request.getSystemName());
        template.setVersion(request.getVersion() != null ? request.getVersion() : "1.0");
        template.setFields(request.getFields());
        template.setTemplateJson(request.getTemplateJson());

        TemplateDocument saved = templateRepository.save(template);
        return mapToResponse(saved);
    }

    public TemplateResponse getTemplateById(String id) {
        TemplateDocument template = templateRepository.findById(id)
                .orElseThrow(() -> new TemplateNotFoundException("Template não encontrado"));
        return mapToResponse(template);
    }

    public TemplateResponse getTemplateByName(String systemName) {
        TemplateDocument template = templateRepository.findBySystemName(systemName)
                .orElseThrow(() -> new TemplateNotFoundException("Template não encontrado"));
        return mapToResponse(template);
    }

    public List<TemplateResponse> getAllTemplates() {
        return templateRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TemplateResponse updateTemplate(String id, Map<String, Object> updates) {
        TemplateDocument template = templateRepository.findById(id)
                .orElseThrow(() -> new TemplateNotFoundException("Template não encontrado"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "systemName" -> template.setSystemName((String) value);
                case "version" -> template.setVersion((String) value);
                case "fields" -> template.setFields((List<TemplateField>) value);
                case "templateJson" -> template.setTemplateJson((Map<String, Object>) value);
            }
        });

        TemplateDocument updated = templateRepository.save(template);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteTemplate(String id) {
        if (!templateRepository.existsById(id)) {
            throw new TemplateNotFoundException("Template não encontrado");
        }
        templateRepository.deleteById(id);
    }

    private TemplateResponse mapToResponse(TemplateDocument template) {
        TemplateResponse response = new TemplateResponse();
        response.setId(template.getId());
        response.setSystemName(template.getSystemName());
        response.setVersion(template.getVersion());
        response.setFields(template.getFields());
        response.setTemplateJson(template.getTemplateJson());
        return response;
    }
}

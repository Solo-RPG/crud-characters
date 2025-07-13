package com.rpgsheets.characterservice.service;

// Certifique-se de que APENAS estas importações estão ativas para o TemplateService para este teste
import com.rpgsheets.characterservice.exception.ResourceNotFoundException;
import com.rpgsheets.characterservice.model.Template;
import com.rpgsheets.characterservice.model.TemplateResponse;
import com.rpgsheets.characterservice.repository.TemplateRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    // O construtor deve injetar APENAS o TemplateRepository para este teste
    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    private TemplateResponse convertToResponse(Template template) {
        TemplateResponse response = new TemplateResponse();
        response.setId(template.getId());
        response.setSystemName(template.getSystemName());
        response.setVersion(template.getVersion());
        response.setFields(template.getFields());
        response.setTemplateJson(template.getTemplateJson());
        return response;
    }

    public Mono<TemplateResponse> getTemplateByName(String systemName) {
        return Mono.justOrEmpty(templateRepository.findBySystemName(systemName))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Template não encontrado no MongoDB local pelo nome: " + systemName)))
                .map(this::convertToResponse);
    }

    public Mono<TemplateResponse> getTemplateById(String templateId) {
        return Mono.justOrEmpty(templateRepository.findById(templateId))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Template não encontrado no MongoDB local pelo ID: " + templateId)))
                .map(this::convertToResponse);
    }
}
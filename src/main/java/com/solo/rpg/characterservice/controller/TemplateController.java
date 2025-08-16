package com.solo.rpg.characterservice.controller;

import com.solo.rpg.characterservice.model.template.TemplateCreate;
import com.solo.rpg.characterservice.model.template.TemplateResponse;
import com.solo.rpg.characterservice.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/templates")
@Tag(name = "Template Controller", description = "Endpoints para gerenciamento de templates de RPG")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo template")
    public ResponseEntity<TemplateResponse> createTemplate(@Valid @RequestBody TemplateCreate request) {
        TemplateResponse response = templateService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter template por ID")
    public ResponseEntity<TemplateResponse> getTemplateById(@PathVariable String id) {
        return ResponseEntity.ok(templateService.getTemplateById(id));
    }

    @GetMapping("/by-name/{systemName}")
    @Operation(summary = "Obter template por nome do sistema")
    public ResponseEntity<TemplateResponse> getTemplateByName(@PathVariable String systemName) {
        return ResponseEntity.ok(templateService.getTemplateByName(systemName));
    }

    @GetMapping
    @Operation(summary = "Listar todos os templates")
    public ResponseEntity<List<TemplateResponse>> getAllTemplates() {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar template")
    public ResponseEntity<TemplateResponse> updateTemplate(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(templateService.updateTemplate(id, updates));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir template")
    public ResponseEntity<Void> deleteTemplate(@PathVariable String id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}

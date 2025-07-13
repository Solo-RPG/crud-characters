package com.rpgsheets.characterservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;  
import java.util.Map;
import java.util.Objects;

// representa os dados de entrada ao criar ou atualizar uma ficha de personagem.
// Inclui anotações de validação para garantir a integridade dos dados recebidos pela API.
public class CharacterSheetRequest {
    private String templateId;
    private String systemName;
    @NotBlank(message = "Owner ID é obrigatório") // Garante que o ownerId não é nulo ou apenas espaços em branco
    private String ownerId;
    @NotNull(message = "Os campos da ficha são obrigatórios") // Garante que o mapa 'fields' não é nulo
    private Map<String, Object> fields; // Os dados dinâmicos da ficha fornecidos pelo usuário

    // Construtor padrão
    public CharacterSheetRequest() {
    }

    // Construtor completo
    public CharacterSheetRequest(String templateId, String systemName, String ownerId, Map<String, Object> fields) {
        this.templateId = templateId;
        this.systemName = systemName;
        this.ownerId = ownerId;
        this.fields = fields;
    }

    // --- Getters e Setters ---
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    // --- Métodos equals, hashCode e toString ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterSheetRequest that = (CharacterSheetRequest) o;
        return Objects.equals(templateId, that.templateId) && Objects.equals(systemName, that.systemName) && Objects.equals(ownerId, that.ownerId) && Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateId, systemName, ownerId, fields);
    }

    @Override
    public String toString() {
        return "CharacterSheetRequest{" +
                "templateId='" + templateId + '\'' +
                ", systemName='" + systemName + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", fields=" + fields +
                '}';
    }
}
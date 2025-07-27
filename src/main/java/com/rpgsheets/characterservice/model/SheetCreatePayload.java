package com.rpgsheets.characterservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

@JsonInclude(Include.NON_NULL) // Não inclua campos nulos no JSON enviado para o Python
public class SheetCreatePayload {
    private String templateId; // ID do template (opcional, se usar systemName)
    private String systemName; // Nome do sistema (opcional, se usar templateId)
    @NotBlank(message = "Owner ID é obrigatório para criar a ficha")
    private String ownerId; // O ID do Character Java que será o dono da ficha
    @NotNull(message = "Os campos da ficha são obrigatórios")
    private Map<String, Object> fields; // Os dados dinâmicos da ficha

    public SheetCreatePayload() {}
    public SheetCreatePayload(String templateId, String systemName, String ownerId, Map<String, Object> fields) {
        this.templateId = templateId;
        this.systemName = systemName;
        this.ownerId = ownerId;
        this.fields = fields;
    }

    // --- Getters e Setters ---
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public Map<String, Object> getFields() { return fields; }
    public void setFields(Map<String, Object> fields) { this.fields = fields; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; SheetCreatePayload that = (SheetCreatePayload) o; return Objects.equals(templateId, that.templateId) && Objects.equals(systemName, that.systemName) && Objects.equals(ownerId, that.ownerId) && Objects.equals(fields, that.fields); }
    @Override public int hashCode() { return Objects.hash(templateId, systemName, ownerId, fields); }
    @Override public String toString() { return "SheetCreatePayload{" + "templateId='" + templateId + '\'' + ", systemName='" + systemName + '\'' + ", ownerId='" + ownerId + '\'' + ", fields=" + fields + '}'; }
}
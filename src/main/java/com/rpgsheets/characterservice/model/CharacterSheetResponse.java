package com.rpgsheets.characterservice.model;

import java.util.Map;
import java.util.Objects;

// DTO para representar a estrutura de resposta da API ao retornar uma ficha de personagem.
// Esta é a forma como os dados serão formatados quando enviados de volta para o cliente.
public class CharacterSheetResponse {
    private String id;
    private String templateId;
    private String templateSystemName;
    private String templateSystemVersion;
    private String ownerId;
    private Map<String, SheetField> data; // Os dados da ficha no formato SheetField

    // Construtor padrão
    public CharacterSheetResponse() {
    }

    // Construtor completo
    public CharacterSheetResponse(String id, String templateId, String templateSystemName, String templateSystemVersion, String ownerId, Map<String, SheetField> data) {
        this.id = id;
        this.templateId = templateId;
        this.templateSystemName = templateSystemName;
        this.templateSystemVersion = templateSystemVersion;
        this.ownerId = ownerId;
        this.data = data;
    }

    // --- Getters e Setters ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateSystemName() {
        return templateSystemName;
    }

    public void setTemplateSystemName(String templateSystemName) {
        this.templateSystemName = templateSystemName;
    }

    public String getTemplateSystemVersion() {
        return templateSystemVersion;
    }

    public void setTemplateSystemVersion(String templateSystemVersion) {
        this.templateSystemVersion = templateSystemVersion;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Map<String, SheetField> getData() {
        return data;
    }

    public void setData(Map<String, SheetField> data) {
        this.data = data;
    }

    // --- Métodos equals, hashCode e toString ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterSheetResponse that = (CharacterSheetResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(templateId, that.templateId) && Objects.equals(templateSystemName, that.templateSystemName) && Objects.equals(templateSystemVersion, that.templateSystemVersion) && Objects.equals(ownerId, that.ownerId) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, templateId, templateSystemName, templateSystemVersion, ownerId, data);
    }

    @Override
    public String toString() {
        return "CharacterSheetResponse{" +
                "id='" + id + '\'' +
                ", templateId='" + templateId + '\'' +
                ", templateSystemName='" + templateSystemName + '\'' +
                ", templateSystemVersion='" + templateSystemVersion + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", data=" + data +
                '}';
    }
}
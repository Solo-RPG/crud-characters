package com.rpgsheets.characterservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Sheet {
    @JsonProperty("id") // Mapeia o campo "id" do JSON do Python
    private String id;
    private String templateId;
    private String templateSystemName;
    private String templateSystemVersion;
    private String ownerId; // Este será o ID do seu Character Java
    private Map<String, SheetField> data; // Dados dinâmicos da ficha (mapa de SheetField)

    public Sheet() {}
    public Sheet(String id, String templateId, String templateSystemName, String templateSystemVersion, String ownerId, Map<String, SheetField> data) {
        this.id = id;
        this.templateId = templateId;
        this.templateSystemName = templateSystemName;
        this.templateSystemVersion = templateSystemVersion;
        this.ownerId = ownerId;
        this.data = data;
    }

    // --- Getters e Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getTemplateSystemName() { return templateSystemName; }
    public void voidsetTemplateSystemName(String templateSystemName) { this.templateSystemName = templateSystemName; }
    public String getTemplateSystemVersion() { return templateSystemVersion; }
    public void setTemplateSystemVersion(String templateSystemVersion) { this.templateSystemVersion = templateSystemVersion; }
    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public Map<String, SheetField> getData() { return data; }
    public void setData(Map<String, SheetField> data) { this.data = data; }

    // --- Métodos equals, hashCode e toString ---
    @Override public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Sheet sheet = (Sheet) o; return Objects.equals(id, sheet.id) && Objects.equals(templateId, sheet.templateId) && Objects.equals(templateSystemName, sheet.templateSystemName) && Objects.equals(templateSystemVersion, sheet.templateSystemVersion) && Objects.equals(ownerId, sheet.ownerId) && Objects.equals(data, sheet.data); }
    @Override public int hashCode() { return Objects.hash(id, templateId, templateSystemName, templateSystemVersion, ownerId, data); }
    @Override public String toString() { return "Sheet{" + "id='" + id + '\'' + ", templateId='" + templateId + '\'' + ", templateSystemName='" + templateSystemName + '\'' + ", templateSystemVersion='" + templateSystemVersion + '\'' + ", ownerId='" + ownerId + '\'' + ", data=" + data + '}'; }
}
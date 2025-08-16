package com.solo.rpg.characterservice.model.template;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "templates")
public class TemplateDocument {
    @Id
    private String id;
    private String systemName;
    private String version;
    private List<TemplateField> fields;
    private Map<String, Object> templateJson;

    // Construtores
    public TemplateDocument() {
    }

    public TemplateDocument(String systemName, String version,
                            List<TemplateField> fields, Map<String, Object> templateJson) {
        this.systemName = systemName;
        this.version = version;
        this.fields = fields;
        this.templateJson = templateJson;
    }

    // Getters e Setters (gerados ou escritos manualmente)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<TemplateField> getFields() {
        return fields;
    }

    public void setFields(List<TemplateField> fields) {
        this.fields = fields;
    }

    public Map<String, Object> getTemplateJson() {
        return templateJson;
    }

    public void setTemplateJson(Map<String, Object> templateJson) {
        this.templateJson = templateJson;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TemplateDocument that = (TemplateDocument) o;
        return Objects.equals(systemName, that.systemName) && Objects.equals(version, that.version) && Objects.equals(fields, that.fields) && Objects.equals(templateJson, that.templateJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, systemName, version, fields, templateJson);
    }

    @Override
    public String toString() {
        return "TemplateDocument{" +
                "id='" + id + '\'' +
                ", systemName='" + systemName + '\'' +
                ", version='" + version + '\'' +
                ", fields=" + fields +
                ", templateJson=" + templateJson +
                '}';
    }
}

package com.rpgsheets.characterservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "templates") // Mapeia para a coleção 'templates' no MongoDB
public class Template {
    @Id
    private String id; // O ID do documento no MongoDB
    private String systemName;
    private String version;
    private List<TemplateField> fields;
    private Map<String, Object> templateJson;

    // Construtor padrão
    public Template() {}

    // Construtor completo
    public Template(String id, String systemName, String version, List<TemplateField> fields, Map<String, Object> templateJson) {
        this.id = id;
        this.systemName = systemName;
        this.version = version;
        this.fields = fields;
        this.templateJson = templateJson;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public List<TemplateField> getFields() { return fields; }
    public void setFields(List<TemplateField> fields) { this.fields = fields; }
    public Map<String, Object> getTemplateJson() { return templateJson; }
    public void setTemplateJson(Map<String, Object> templateJson) { this.templateJson = templateJson; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Template template = (Template) o;
        return Objects.equals(id, template.id) && Objects.equals(systemName, template.systemName) && Objects.equals(version, template.version) && Objects.equals(fields, template.fields) && Objects.equals(templateJson, template.templateJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, systemName, version, fields, templateJson);
    }

    @Override
    public String toString() {
        return "Template{" +
                "id='" + id + '\'' +
                ", systemName='" + systemName + '\'' +
                ", version=" + version +
                ", fields=" + fields +
                ", templateJson=" + templateJson +
                '}';
    }
}
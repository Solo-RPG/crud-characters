package com.solo.rpg.characterservice.model.template;

import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TemplateUpdate {
    @Size(min = 3)
    private String systemName;

    private String version;

    private List<TemplateField> fields;

    private Map<String, Object> templateJson;

    public TemplateUpdate() {
    }

    public TemplateUpdate(String systemName, String version,
                          List<TemplateField> fields, Map<String, Object> templateJson) {
        this.systemName = systemName;
        this.version = version;
        this.fields = fields;
        this.templateJson = templateJson;
    }

    // Getters and Setters
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateUpdate that = (TemplateUpdate) o;
        return Objects.equals(systemName, that.systemName) &&
                Objects.equals(version, that.version) &&
                Objects.equals(fields, that.fields) &&
                Objects.equals(templateJson, that.templateJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemName, version, fields, templateJson);
    }

    @Override
    public String toString() {
        return "TemplateUpdate{" +
                "systemName='" + systemName + '\'' +
                ", version='" + version + '\'' +
                ", fields=" + fields +
                ", templateJson=" + templateJson +
                '}';
    }
}

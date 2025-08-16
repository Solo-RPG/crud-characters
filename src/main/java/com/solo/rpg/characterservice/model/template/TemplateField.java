package com.solo.rpg.characterservice.model.template;

import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TemplateField {
    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "string|number|boolean|list|object")
    private String type;

    private boolean required = true;

    private Object defaultValue;

    private List<String> options;

    private List<TemplateField> fields;

    public TemplateField() {
    }

    public TemplateField(String name, String type, boolean required, Object defaultValue,
                         List<String> options, List<TemplateField> fields) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.defaultValue = defaultValue;
        this.options = options;
        this.fields = fields;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<TemplateField> getFields() {
        return fields;
    }

    public void setFields(List<TemplateField> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateField that = (TemplateField) o;
        return required == that.required &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(defaultValue, that.defaultValue) &&
                Objects.equals(options, that.options) &&
                Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, required, defaultValue, options, fields);
    }

    @Override
    public String toString() {
        return "TemplateField{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", required=" + required +
                ", defaultValue=" + defaultValue +
                ", options=" + options +
                ", fields=" + fields +
                '}';
    }
}

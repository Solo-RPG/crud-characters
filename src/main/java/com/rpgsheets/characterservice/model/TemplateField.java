package com.rpgsheets.characterservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.Objects;

// Representa um campo dentro de um template de ficha, conforme retornado pelo serviço de templates Python
public class TemplateField {
    private String name;
    private FieldType type; // Usamos um enum para os tipos de campo
    private boolean required = true;
    private Object defaultValue; // Usamos Object para flexibilidade (String, int, float, boolean, etc.)
    private List<String> options;
    private List<TemplateField> fields; // Para campos aninhados (recursividade)

    // Construtor padrão (necessário para que o Jackson, usado pelo Spring, possa deserializar JSON para esta classe)
    public TemplateField() {
    }

    // Construtor completo para criar objetos TemplateField
    public TemplateField(String name, FieldType type, boolean required, Object defaultValue, List<String> options, List<TemplateField> fields) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.defaultValue = defaultValue;
        this.options = options;
        this.fields = fields;
    }

    // --- Getters e Setters ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
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

    // --- Métodos equals, hashCode e toString (importante para comparações e logs) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateField that = (TemplateField) o;
        return required == that.required && Objects.equals(name, that.name) && type == that.type && Objects.equals(defaultValue, that.defaultValue) && Objects.equals(options, that.options) && Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, required, defaultValue, options, fields);
    }

    @Override
    public String toString() {
        return "TemplateField{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", required=" + required +
                ", defaultValue=" + defaultValue +
                ", options=" + options +
                ", fields=" + fields +
                '}';
    }
}
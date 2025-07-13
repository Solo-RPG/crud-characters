package com.rpgsheets.characterservice.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

// campo de dado *real* dentro de uma ficha de personagem salva no MongoDB.
// equivalente ao SheetField Python.
public class SheetField {
    private Object value; // O valor pode ser String, Integer, Double, Boolean, Map<String, SheetField> (para aninhados), ou List
    private boolean required = true; // Se o campo era obrigatório no template
    private List<String> options; // Opções pré-definidas para o campo

    // Construtor padrão
    public SheetField() {
    }

    // Construtor completo
    public SheetField(Object value, boolean required, List<String> options) {
        this.value = value;
        this.required = required;
        this.options = options;
    }

    // --- Getters e Setters ---
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    // --- Métodos equals, hashCode e toString ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SheetField that = (SheetField) o;
        return required == that.required && Objects.equals(value, that.value) && Objects.equals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, required, options);
    }

    @Override
    public String toString() {
        return "SheetField{" +
                "value=" + value +
                ", required=" + required +
                ", options=" + options +
                '}';
    }
}
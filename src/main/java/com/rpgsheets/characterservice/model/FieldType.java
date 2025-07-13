package com.rpgsheets.characterservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// Enum para mapear os tipos de campo definidos no seu template Python
public enum FieldType {
    STRING("string"),
    NUMBER("number"),
    BOOLEAN("boolean"),
    LIST("list"),
    OBJECT("object");

    private final String value;

    FieldType(String value) {
        this.value = value;
    }

    @JsonValue // método deve ser usado ao serializar o enum para JSON (ex: STRING -> "string")
    public String getValue() {
        return value;
    }

    @JsonCreator // método deve ser usado ao deserializar de JSON para o enum (ex: "string" -> STRING)
    public static FieldType fromValue(String value) {
        for (FieldType type : FieldType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de campo desconhecido: " + value);
    }
}
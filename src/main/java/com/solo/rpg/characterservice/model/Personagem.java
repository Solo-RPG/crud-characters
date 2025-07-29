package com.solo.rpg.characterservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

// Anotações Lombok para getters, setters, construtores, toString, etc.
@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor // Gera construtor com todos os argumentos
@Document(collection = "personagens") // Mapeia para a coleção "personagens" no MongoDB
public class Personagem {

    @Id // Marca o campo como ID do documento no MongoDB (_id)
    private String id; // O MongoDB usa _id, que o Spring Data mapeia para "id" por padrão

    private String templateId; // ID do template de RPG usado (ex: D&D 5e)
    private String templateSystemName; // Nome do sistema de RPG (ex: "D&D 5e")
    private String templateSystemVersion; // Versão do sistema de RPG (ex: "1.0")
    private String ownerId; // ID do dono do personagem (usuário)


    private Map<String, Object> data; // Dados dinâmicos do personagem


}
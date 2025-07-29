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

    // O campo 'data' é um mapa dinâmico, semelhante ao seu 'Dict[str, SheetField]'
    // No Java, representamos isso com um Map<String, Object> ou Map<String, Map<String, Object>>
    // Vamos usar Map<String, Object> para simplificar inicialmente, pois o Spring Data MongoDB
    // consegue persistir estruturas aninhadas de Map.
    private Map<String, Object> data; // Dados dinâmicos do personagem

    // No seu Python, SheetField tinha value, required, options.
    // Para simplificar no Java, e seguindo a forma como os dados são consumidos em 'data',
    // podemos deixar 'data' como um mapa genérico e o serviço Python de 'templates'
    // será o responsável pela validação da estrutura interna.
    // Se precisássemos validar a estrutura interna de 'SheetField' no Java,
    // criaríamos uma classe SheetField e SheetFieldValue (como em Python)
    // e usaríamos Map<String, SheetField> aqui. Por agora, Map<String, Object> é mais flexível.
}
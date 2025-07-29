package com.solo.rpg.characterservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "personagens") // Indica que mapeia para a coleção "personagens" no MongoDB
public class Personagem {

    @Id // Marca este campo como o ID do documento no MongoDB (_id)
    private String id; // ID ÚNICO deste objeto Personagem (gerado pelo banco de dados/aplicação)
    private String ownerId; // ID do usuário que possui este personagem (para filtrar por usuário)
    private String nomePersonagem; // Nome do personagem (para identificação)

    // Referência cruzada: ID da ficha associada a este personagem, que vive no serviço Python de Fichas
    private String fichaId;
}
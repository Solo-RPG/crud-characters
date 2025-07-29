package com.solo.rpg.characterservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// DTO para a requisição de criação de Personagem
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonagemCreateRequest {
    private String ownerId;
    private String nomePersonagem;
    private String fichaId; // Opcional, se a ficha puder ser atribuída depois
}
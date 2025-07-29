package com.solo.rpg.characterservice.service;

import com.solo.rpg.characterservice.model.Personagem;
import com.solo.rpg.characterservice.model.PersonagemCreateRequest;
import com.solo.rpg.characterservice.repository.PersonagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonagemService {

    private final PersonagemRepository personagemRepository;

    @Autowired
    public PersonagemService(PersonagemRepository personagemRepository) {
        this.personagemRepository = personagemRepository;
    }

    /**
     * Cria um novo personagem. fichaId agora é opcional na criação.
     * @param request DTO com ownerId, nomePersonagem, historia, imagem e opcionalmente fichaId.
     * @return O objeto Personagem salvo no banco de dados.
     */
    public Personagem createPersonagem(PersonagemCreateRequest request) {
        if (request.getOwnerId() == null || request.getOwnerId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ownerId é obrigatório.");
        }
        if (request.getNomePersonagem() == null || request.getNomePersonagem().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nomePersonagem é obrigatório.");
        }
        // >>>>> VALIDAÇÃO DE FICHAID REMOVIDA PARA PERMITIR CRIAÇÃO SEM FICHA <<<<<
        // if (request.getFichaId() == null || request.getFichaId().isEmpty()) {
        //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fichaId é obrigatório para criar um personagem.");
        // }

        Personagem novoPersonagem = new Personagem();
        novoPersonagem.setId(UUID.randomUUID().toString());
        novoPersonagem.setOwnerId(request.getOwnerId());
        novoPersonagem.setNomePersonagem(request.getNomePersonagem());
        novoPersonagem.setFichaId(request.getFichaId());
        novoPersonagem.setHistoria(request.getHistoria());
        novoPersonagem.setImagem(request.getImagem());

        return personagemRepository.save(novoPersonagem);
    }

    /**
     * Busca um personagem pelo seu ID único.
     * @param id O ID do personagem.
     * @return Um Optional contendo o Personagem se encontrado, ou Optional.empty() caso contrário.
     */
    public Optional<Personagem> getPersonagemById(String id) {
        return personagemRepository.findById(id);
    }

    /**
     * Retorna uma lista de todos os personagens no banco de dados.
     * @return Uma lista de objetos Personagem.
     */
    public List<Personagem> getAllPersonagens() {
        return personagemRepository.findAll();
    }

    /**
     * Busca uma lista de personagens pelo ID do seu proprietário (usuário).
     * @param ownerId O ID do usuário proprietário.
     * @return Uma lista de objetos Personagem pertencentes ao ownerId.
     */
    public List<Personagem> getPersonagensByOwnerId(String ownerId) {
        return personagemRepository.findByOwnerId(ownerId);
    }

    /**
     * Atualiza um personagem existente com os dados fornecidos.
     * Inclui agora 'historia' e 'imagem' na atualização.
     * @param id O ID do personagem a ser atualizado.
     * @param updateData Um mapa contendo os campos e novos valores para atualização.
     * @return O objeto Personagem atualizado, ou null se não for encontrado.
     */
    public Personagem updatePersonagem(String id, Map<String, Object> updateData) {
        Optional<Personagem> existingPersonagemOptional = personagemRepository.findById(id);

        if (existingPersonagemOptional.isEmpty()) {
            return null;
        }

        Personagem existingPersonagem = existingPersonagemOptional.get();

        if (updateData.containsKey("ownerId")) {
            existingPersonagem.setOwnerId((String) updateData.get("ownerId"));
        }
        if (updateData.containsKey("nomePersonagem")) {
            existingPersonagem.setNomePersonagem((String) updateData.get("nomePersonagem"));
        }
        if (updateData.containsKey("fichaId")) {
            existingPersonagem.setFichaId((String) updateData.get("fichaId"));
        }
        if (updateData.containsKey("historia")) {
            existingPersonagem.setHistoria((String) updateData.get("historia"));
        }
        if (updateData.containsKey("imagem")) {
            existingPersonagem.setImagem((String) updateData.get("imagem"));
        }

        return personagemRepository.save(existingPersonagem);
    }

    /**
     * Atribui (ou atualiza) um fichaId para um personagem existente.
     * @param personagemId O ID do personagem a ser atualizado.
     * @param fichaId O ID da ficha a ser vinculado.
     * @return O personagem atualizado.
     */
    public Personagem assignFichaToPersonagem(String personagemId, String fichaId) {
        Optional<Personagem> personagemOptional = personagemRepository.findById(personagemId);
        if (personagemOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado.");
        }

        Personagem personagem = personagemOptional.get();
        personagem.setFichaId(fichaId);

        return personagemRepository.save(personagem);
    }

    /**
     * Deleta um personagem pelo seu ID único.
     * @param id O ID do personagem a ser deletado.
     * @return true se o personagem foi deletado com sucesso, false caso contrário (não encontrado).
     */
    public boolean deletePersonagem(String id) {
        if (personagemRepository.existsById(id)) {
            personagemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
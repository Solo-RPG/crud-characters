package com.solo.rpg.characterservice.service;

import com.solo.rpg.characterservice.model.character.Personagem;
import com.solo.rpg.characterservice.model.character.PersonagemCreateRequest;
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

    public Personagem createPersonagem(PersonagemCreateRequest request, String ownerId) { // RECEBE O E-MAIL COMO PARÂMETRO
        if (request.getNomePersonagem() == null || request.getNomePersonagem().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nomePersonagem é obrigatório.");
        }

        Personagem novoPersonagem = new Personagem();
        novoPersonagem.setId(UUID.randomUUID().toString());
        novoPersonagem.setOwnerId(ownerId); // PREENCHE O ownerId COM O ID DO USUÁRIO LOGADO
        novoPersonagem.setNomePersonagem(request.getNomePersonagem());
        novoPersonagem.setFichaId(request.getFichaId());
        novoPersonagem.setHistoria(request.getHistoria());
        novoPersonagem.setImagem(request.getImagem());

        return personagemRepository.save(novoPersonagem);
    }

    public Optional<Personagem> getPersonagemById(String id) {
        return personagemRepository.findById(id);
    }

    public List<Personagem> getAllPersonagens() {
        return personagemRepository.findAll();
    }

    public List<Personagem> getPersonagensByOwnerId(String ownerId) {
        return personagemRepository.findByOwnerId(ownerId);
    }

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

    public Personagem assignFichaToPersonagem(String personagemId, String fichaId) {
        Optional<Personagem> personagemOptional = personagemRepository.findById(personagemId);
        if (personagemOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado.");
        }

        Personagem personagem = personagemOptional.get();
        personagem.setFichaId(fichaId);

        return personagemRepository.save(personagem);
    }

    public boolean deletePersonagem(String id) {
        if (personagemRepository.existsById(id)) {
            personagemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
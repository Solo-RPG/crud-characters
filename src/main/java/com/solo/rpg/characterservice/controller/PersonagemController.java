package com.solo.rpg.characterservice.controller;

import com.solo.rpg.characterservice.model.Personagem;
import com.solo.rpg.characterservice.service.PersonagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController // Indica que esta classe é um controlador REST
@RequestMapping("/api/characters") // Define o prefixo base para todos os endpoints neste controller
public class PersonagemController {

    private final PersonagemService personagemService;

    // Injeção de dependência do serviço
    @Autowired
    public PersonagemController(PersonagemService personagemService) {
        this.personagemService = personagemService;
    }

    // POST /api/characters/
    @PostMapping
    public ResponseEntity<Personagem> createPersonagem(@RequestBody Map<String, Object> request) {
        // Normalização dos dados, como feito no seu sheets.py
        String templateId = (String) request.get("template_id");
        String systemName = (String) request.get("system_name");
        String ownerId = (String) request.get("owner_id");
        Map<String, Object> fields = (Map<String, Object>) request.get("fields");

        // Validações básicas (replicando as do Python)
        if ((templateId == null || templateId.isEmpty()) && (systemName == null || systemName.isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pelo menos um identificador (template_id ou system_name) deve ser fornecido");
        }
        if (ownerId == null || ownerId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "owner_id é obrigatório");
        }
        if (fields == null || fields.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fields não pode ser vazio");
        }
        if (!(fields instanceof Map)) { // Verifica se é um dicionário/mapa
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fields deve ser um dicionário");
        }

        try {
            Personagem novoPersonagem = personagemService.createPersonagem(ownerId, templateId, systemName, fields);
            return new ResponseEntity<>(novoPersonagem, HttpStatus.CREATED); // Retorna 201 Created
        } catch (ResponseStatusException e) {
            throw e; // Re-lança a exceção HTTP já com o status correto
        } catch (Exception e) {
            // Log do erro completo para depuração
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar personagem: " + e.getMessage());
        }
    }

    // GET /api/characters/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Personagem> getPersonagemById(@PathVariable String id) {
        Optional<Personagem> personagem = personagemService.getPersonagemById(id);
        return personagem.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado"));
    }

    // GET /api/characters/
    @GetMapping
    public ResponseEntity<List<Personagem>> getAllPersonagens() {
        List<Personagem> personagens = personagemService.getAllPersonagens();
        return new ResponseEntity<>(personagens, HttpStatus.OK);
    }

    // GET /api/characters/by-owner/{ownerId}
    @GetMapping("/by-owner/{ownerId}")
    public ResponseEntity<List<Personagem>> getPersonagensByOwnerId(@PathVariable String ownerId) {
        List<Personagem> personagens = personagemService.getPersonagensByOwnerId(ownerId);
        if (personagens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum personagem encontrado para o proprietário: " + ownerId);
        }
        return new ResponseEntity<>(personagens, HttpStatus.OK);
    }

    // PUT /api/characters/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Personagem> updatePersonagem(@PathVariable String id, @RequestBody Map<String, Object> updateData) {
        if (updateData == null || updateData.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhum dado para atualizar fornecido");
        }

        Personagem updatedPersonagem = personagemService.updatePersonagem(id, updateData);
        if (updatedPersonagem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado ou nenhum dado foi alterado");
        }
        return new ResponseEntity<>(updatedPersonagem, HttpStatus.OK);
    }

    // DELETE /api/characters/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonagem(@PathVariable String id) {
        boolean deleted = personagemService.deletePersonagem(id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado");
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retorna 204 No Content para deleção bem-sucedida
    }
}
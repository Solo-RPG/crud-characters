package com.solo.rpg.characterservice.controller;

import com.solo.rpg.characterservice.model.Personagem;
import com.solo.rpg.characterservice.model.PersonagemCreateRequest; // Importe esta nova classe
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

    // Endpoint para CRIAR um novo personagem
    // Aceita um PersonagemCreateRequest com ownerId, nomePersonagem e fichaId
    @PostMapping
    public ResponseEntity<Personagem> createPersonagem(@RequestBody PersonagemCreateRequest request) {
        try {
            Personagem novoPersonagem = personagemService.createPersonagem(request);
            return new ResponseEntity<>(novoPersonagem, HttpStatus.CREATED); // Retorna 201 Created
        } catch (ResponseStatusException e) {
            // Re-lança exceções do serviço que já contêm status HTTP (ex: BAD_REQUEST)
            throw e;
        } catch (Exception e) {
            // Captura outras exceções inesperadas e retorna 500 Internal Server Error
            e.printStackTrace(); // Imprime o stack trace no console para depuração
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar personagem: " + e.getMessage());
        }
    }

    // Endpoint para BUSCAR um personagem pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Personagem> getPersonagemById(@PathVariable String id) {
        Optional<Personagem> personagem = personagemService.getPersonagemById(id);
        // Retorna 200 OK se encontrado, ou 404 Not Found se não encontrado
        return personagem.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado"));
    }

    // Endpoint para LISTAR TODOS os personagens
    @GetMapping
    public ResponseEntity<List<Personagem>> getAllPersonagens() {
        List<Personagem> personagens = personagemService.getAllPersonagens();
        return new ResponseEntity<>(personagens, HttpStatus.OK);
    }

    // Endpoint para LISTAR personagens por ID do proprietário (ownerId)
    @GetMapping("/by-owner/{ownerId}")
    public ResponseEntity<List<Personagem>> getPersonagensByOwnerId(@PathVariable String ownerId) {
        List<Personagem> personagens = personagemService.getPersonagensByOwnerId(ownerId);
        // Pode retornar uma lista vazia ou 404, dependendo da sua preferência.
        // Aqui, optei por 404 se não houver nenhum personagem para o ownerId.
        if (personagens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum personagem encontrado para o proprietário: " + ownerId);
        }
        return new ResponseEntity<>(personagens, HttpStatus.OK);
    }

    // Endpoint para ATUALIZAR um personagem pelo ID
    // O RequestBody é um Map<String, Object> para flexibilidade, permitindo atualização parcial
    @PutMapping("/{id}")
    public ResponseEntity<Personagem> updatePersonagem(@PathVariable String id, @RequestBody Map<String, Object> updateData) {
        if (updateData == null || updateData.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhum dado para atualizar fornecido");
        }

        Personagem updatedPersonagem = personagemService.updatePersonagem(id, updateData);
        if (updatedPersonagem == null) {
            // Retorna 404 Not Found se o personagem não for encontrado pelo ID
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado ou nenhum dado foi alterado");
        }
        return new ResponseEntity<>(updatedPersonagem, HttpStatus.OK);
    }

    // Endpoint para DELETAR um personagem pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonagem(@PathVariable String id) {
        boolean deleted = personagemService.deletePersonagem(id);
        if (!deleted) {
            // Retorna 404 Not Found se o personagem não for encontrado pelo ID
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado");
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retorna 204 No Content para deleção bem-sucedida
    }
}
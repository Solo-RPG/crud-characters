package com.solo.rpg.characterservice.controller;

import com.solo.rpg.characterservice.model.Personagem;
import com.solo.rpg.characterservice.model.PersonagemCreateRequest;
import com.solo.rpg.characterservice.service.PersonagemService;
import io.swagger.v3.oas.annotations.Operation; // Mantendo para Swagger, mesmo sem Lombok
import io.swagger.v3.oas.annotations.Parameter; // Mantendo para Swagger
import io.swagger.v3.oas.annotations.media.Content; // Mantendo para Swagger
import io.swagger.v3.oas.annotations.media.Schema; // Mantendo para Swagger
import io.swagger.v3.oas.annotations.responses.ApiResponse; // Mantendo para Swagger
import io.swagger.v3.oas.annotations.responses.ApiResponses; // Mantendo para Swagger
import io.swagger.v3.oas.annotations.tags.Tag; // Mantendo para Swagger
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/characters")
@Tag(name = "Personagem Controller", description = "Endpoints para gerenciamento de personagens de RPG")
public class PersonagemController {

    private final PersonagemService personagemService;

    @Autowired
    public PersonagemController(PersonagemService personagemService) {
        this.personagemService = personagemService;
    }

    @Operation(summary = "Criar um novo personagem",
            description = "Cria um novo personagem com os dados fornecidos. A ligação com a ficha é opcional na criação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Personagem criado com sucesso",
                    content = @Content(schema = @Schema(implementation = Personagem.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<Personagem> createPersonagem(@RequestBody PersonagemCreateRequest request) {
        try {
            Personagem novoPersonagem = personagemService.createPersonagem(request);
            return new ResponseEntity<>(novoPersonagem, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado ao criar personagem: " + e.getMessage());
        }
    }

    @Operation(summary = "Obter um personagem por ID",
            description = "Retorna os detalhes de um personagem específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personagem encontrado",
                    content = @Content(schema = @Schema(implementation = Personagem.class))),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Personagem> getPersonagemById(
            @Parameter(description = "ID do personagem a ser buscado", required = true)
            @PathVariable String id) {
        Optional<Personagem> personagem = personagemService.getPersonagemById(id);
        return personagem.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado"));
    }

    @Operation(summary = "Listar todos os personagens",
            description = "Retorna uma lista de todos os personagens cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de personagens retornada com sucesso",
            content = @Content(schema = @Schema(implementation = Personagem.class)))
    @GetMapping
    public ResponseEntity<List<Personagem>> getAllPersonagens() {
        List<Personagem> personagens = personagemService.getAllPersonagens();
        return new ResponseEntity<>(personagens, HttpStatus.OK);
    }

    @Operation(summary = "Listar personagens por proprietário",
            description = "Retorna todos os personagens associados a um determinado ownerId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personagens encontrados",
                    content = @Content(schema = @Schema(implementation = Personagem.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum personagem encontrado para o proprietário")
    })
    @GetMapping("/by-owner/{ownerId}")
    public ResponseEntity<List<Personagem>> getPersonagensByOwnerId(
            @Parameter(description = "ID do proprietário dos personagens", required = true)
            @PathVariable String ownerId) {
        List<Personagem> personagens = personagemService.getPersonagensByOwnerId(ownerId);
        if (personagens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum personagem encontrado para o proprietário: " + ownerId);
        }
        return new ResponseEntity<>(personagens, HttpStatus.OK);
    }

    @Operation(summary = "Atualizar um personagem",
            description = "Atualiza parcial ou totalmente os dados de um personagem existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personagem atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Personagem.class))),
            @ApiResponse(responseCode = "400", description = "Dados de atualização inválidos ou vazios"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Personagem> updatePersonagem(
            @Parameter(description = "ID do personagem a ser atualizado", required = true)
            @PathVariable String id,
            @Parameter(description = "Dados de atualização no formato chave-valor")
            @RequestBody Map<String, Object> updateData) {
        if (updateData == null || updateData.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhum dado para atualizar fornecido");
        }

        Personagem updatedPersonagem = personagemService.updatePersonagem(id, updateData);
        if (updatedPersonagem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado ou nenhum dado foi alterado");
        }
        return new ResponseEntity<>(updatedPersonagem, HttpStatus.OK);
    }

    @Operation(summary = "Vincular ficha a um personagem",
            description = "Associa um fichaId a um personagem existente. Ideal para vincular fichas criadas separadamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ficha vinculada com sucesso",
                    content = @Content(schema = @Schema(implementation = Personagem.class))),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao vincular ficha")
    })
    @PutMapping("/{personagemId}/assign-ficha/{fichaId}")
    public ResponseEntity<Personagem> assignFichaToPersonagem(
            @Parameter(description = "ID do personagem a ser atualizado", required = true)
            @PathVariable String personagemId,
            @Parameter(description = "ID da ficha a ser vinculada ao personagem", required = true)
            @PathVariable String fichaId) {
        try {
            Personagem updatedPersonagem = personagemService.assignFichaToPersonagem(personagemId, fichaId);
            return new ResponseEntity<>(updatedPersonagem, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao vincular ficha ao personagem: " + e.getMessage());
        }
    }


    @Operation(summary = "Excluir um personagem",
            description = "Remove permanentemente um personagem do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Personagem excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonagem(
            @Parameter(description = "ID do personagem a ser excluído", required = true)
            @PathVariable String id) {
        boolean deleted = personagemService.deletePersonagem(id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado");
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
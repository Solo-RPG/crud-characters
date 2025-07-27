package com.rpgsheets.characterservice.controller;

import com.rpgsheets.characterservice.model.CharacterRequest;
import com.rpgsheets.characterservice.model.CharacterResponse;
import com.rpgsheets.characterservice.model.Sheet; // Importa o modelo de Ficha do serviço Python
import com.rpgsheets.characterservice.model.SheetCreatePayload; // Importa o DTO para criar Ficha no Python
import com.rpgsheets.characterservice.service.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/characters") // URL base para o CRUD de Personagens
@Tag(name = "Personagens", description = "API para gerenciamento de personagens e suas fichas associadas")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    // --- Endpoints CRUD para Personagens ---

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria um novo personagem")
    public Mono<CharacterResponse> createCharacter(@Valid @RequestBody CharacterRequest request) {
        return characterService.createCharacter(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um personagem por ID")
    public Mono<CharacterResponse> getCharacterById(@Parameter(description = "ID do personagem") @PathVariable String id) {
        return characterService.getCharacterById(id);
    }

    @GetMapping
    @Operation(summary = "Lista todos os personagens (paginada)")
    public Mono<Page<CharacterResponse>> getAllCharacters(Pageable pageable) {
        return characterService.getAllCharacters(pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um personagem existente")
    public Mono<CharacterResponse> updateCharacter(
            @Parameter(description = "ID do personagem a ser atualizado") @PathVariable String id,
            @Valid @RequestBody CharacterRequest request) {
        return characterService.updateCharacter(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um personagem")
    public Mono<Void> deleteCharacter(@Parameter(description = "ID do personagem a ser excluído") @PathVariable String id) {
        return characterService.deleteCharacter(id);
    }

    // --- Endpoints para Interação com o Serviço Python de Fichas ---

    @PostMapping("/{characterId}/sheets") // Endpoint para criar uma ficha e associá-la a um personagem
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria uma nova ficha de RPG no serviço Python e associa ao personagem",
            description = "Envia os dados para o serviço Python de fichas para criar uma nova ficha e armazena o ID da ficha no personagem Java.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ficha criada e associada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida ou erro na criação da ficha no serviço Python"),
                    @ApiResponse(responseCode = "404", description = "Personagem não encontrado para associação"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            })
    public Mono<Sheet> createSheetForCharacter(
            @Parameter(description = "ID do personagem ao qual a ficha será associada") @PathVariable String characterId,
            @Valid @RequestBody SheetCreatePayload request) {
        return characterService.createSheetForCharacter(characterId, request);
    }

    @GetMapping("/{characterId}/sheets") // Endpoint para listar as fichas de um personagem
    @Operation(summary = "Lista todas as fichas de RPG associadas a um personagem",
            description = "Busca as fichas completas no serviço Python de fichas usando o ID do personagem como ownerId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Fichas retornadas com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor ou no serviço de fichas")
            })
    public Flux<Sheet> getSheetsByCharacterId(@Parameter(description = "ID do personagem cujas fichas serão buscadas") @PathVariable String characterId) {
        return characterService.getSheetsByCharacterId(characterId);
    }
}
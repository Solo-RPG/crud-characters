package com.rpgsheets.characterservice.controller;

import com.rpgsheets.characterservice.model.CharacterSheetRequest;
import com.rpgsheets.characterservice.model.CharacterSheetResponse;
import com.rpgsheets.characterservice.service.CharacterSheetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/characters") // Define a URL base
@Tag(name = "Fichas de Personagens", description = "API para gerenciamento de fichas de personagens de RPG") // Anotação do Swagger para organizar a documentação
public class CharacterSheetController {

    private final CharacterSheetService characterSheetService; // lógica de negócio

    // Injeção de dependência do serviço CharacterSheetService via construtor
    public CharacterSheetController(CharacterSheetService characterSheetService) {
        this.characterSheetService = characterSheetService;
    }

    @PostMapping // Mapeia requisições HTTP POST para /api/characters
    @ResponseStatus(HttpStatus.CREATED) // Retorna status HTTP 201 Created em caso de sucesso
    @Operation(summary = "Cria uma nova ficha de personagem", // Descrição para o Swagger UI
            description = "Cria uma nova ficha de personagem baseada em um template existente e nos dados fornecidos.",
            responses = { // Respostas esperadas para a API
                    @ApiResponse(responseCode = "201", description = "Ficha criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (template não encontrado, dados incompletos/inválidos)"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            })
    public Mono<CharacterSheetResponse> createCharacterSheet(@Valid @RequestBody CharacterSheetRequest request) {
        return characterSheetService.createCharacterSheet(request);
    }

    @GetMapping("/{id}") // Mapeia requisições HTTP GET para /api/characters/{id}
    @Operation(summary = "Busca uma ficha de personagem por ID",
            description = "Retorna os detalhes de uma ficha de personagem específica.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ficha encontrada"),
                    @ApiResponse(responseCode = "404", description = "Ficha não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            })
    public Mono<CharacterSheetResponse> getCharacterSheetById(
            @Parameter(description = "ID da ficha do personagem") @PathVariable String id) {
        // @PathVariable extrai o ID da URL
        return characterSheetService.getCharacterSheetById(id);
    }

    @GetMapping // Mapeia requisições HTTP GET para /api/characters (para listagem paginada)
    @Operation(summary = "Lista todas as fichas de personagem (paginada)",
            description = "Retorna uma lista paginada de todas as fichas de personagem. Use os parâmetros 'page', 'size' e 'sort' na URL (ex: ?page=0&size=10&sort=ownerId,asc).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de fichas retornada com sucesso"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            })
    public Mono<Page<CharacterSheetResponse>> getAllCharacterSheets(Pageable pageable) {
        // O objeto Pageable é injetado automaticamente pelo Spring, baseado em parâmetros de URL como `?page=0&size=10&sort=fieldName,asc`
        return characterSheetService.getAllCharacterSheets(pageable);
    }

    @PutMapping("/{id}") // Mapeia requisições HTTP PUT para /api/characters/{id}
    @Operation(summary = "Atualiza uma ficha de personagem existente",
            description = "Atualiza os dados de uma ficha de personagem específica por ID. Os dados da requisição serão validados contra o template atual da ficha.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ficha atualizada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (dados incompletos/inválidos)"),
                    @ApiResponse(responseCode = "404", description = "Ficha não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            })
    public Mono<CharacterSheetResponse> updateCharacterSheet(
            @Parameter(description = "ID da ficha do personagem a ser atualizada") @PathVariable String id,
            @Valid @RequestBody CharacterSheetRequest request) {
        return characterSheetService.updateCharacterSheet(id, request);
    }

    @DeleteMapping("/{id}") // Mapeia requisições HTTP DELETE para /api/characters/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna status HTTP 204 No Content (sucesso sem corpo de resposta)
    @Operation(summary = "Exclui uma ficha de personagem",
            description = "Remove uma ficha de personagem do sistema por ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Ficha excluída com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Ficha não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            })
    public Mono<Void> deleteCharacterSheet(
            @Parameter(description = "ID da ficha do personagem a ser excluída") @PathVariable String id) {
        return characterSheetService.deleteCharacterSheet(id);
    }
}
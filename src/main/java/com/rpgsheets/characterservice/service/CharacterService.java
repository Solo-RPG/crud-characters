package com.rpgsheets.characterservice.service;

import com.rpgsheets.characterservice.config.ApplicationConfig;
import com.rpgsheets.characterservice.exception.BadRequestException;
import com.rpgsheets.characterservice.exception.ResourceNotFoundException;
import com.rpgsheets.characterservice.model.Character;
import com.rpgsheets.characterservice.model.CharacterRequest;
import com.rpgsheets.characterservice.model.CharacterResponse;
import com.rpgsheets.characterservice.repository.CharacterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Importa os modelos que representam a estrutura dos dados do serviço Python de Fichas
import com.rpgsheets.characterservice.model.Sheet;
import com.rpgsheets.characterservice.model.SheetCreatePayload;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final WebClient sheetsWebClient; // WebClient para comunicar com o serviço Python de Fichas

    public CharacterService(CharacterRepository characterRepository, ApplicationConfig appConfig) {
        this.characterRepository = characterRepository;
        // Configura o WebClient com a URL base do serviço Python de Fichas
        this.sheetsWebClient = WebClient.builder()
                .baseUrl(appConfig.getSheetsServiceUrl())
                .build();
    }

    // --- Métodos CRUD para Personagens (no MongoDB do Java) ---

    public Mono<CharacterResponse> createCharacter(CharacterRequest request) {
        Character character = new Character();
        character.setName(request.getName());
        character.setPlayerUserId(request.getPlayerUserId());
        // Se IDs de ficha forem fornecidos na criação do personagem, associá-los
        character.setSheetIds(request.getSheetIds() != null ? new ArrayList<>(request.getSheetIds()) : new ArrayList<>());

        return characterRepository.save(character)
                .map(this::convertToResponse); // Converte a entidade salva para DTO de resposta
    }

    public Mono<CharacterResponse> getCharacterById(String id) {
        return characterRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Personagem não encontrado com ID: " + id)))
                .map(this::convertToResponse);
    }

    public Mono<Page<CharacterResponse>> getAllCharacters(Pageable pageable) {
        return characterRepository.findAll(pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize())
                .collectList()
                .zipWith(characterRepository.count()) // Combina a lista com a contagem total para paginação
                .map(tuple -> {
                    List<CharacterResponse> content = tuple.getT1().stream()
                            .map(this::convertToResponse)
                            .collect(Collectors.toList());
                    return new PageImpl<>(content, pageable, tuple.getT2());
                });
    }

    public Mono<CharacterResponse> updateCharacter(String id, CharacterRequest request) {
        return characterRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Personagem não encontrado com ID: " + id)))
                .flatMap(existingCharacter -> {
                    existingCharacter.setName(request.getName());
                    existingCharacter.setPlayerUserId(request.getPlayerUserId());
                    // Atualiza a lista de sheetIds se uma nova lista for fornecida
                    if (request.getSheetIds() != null) {
                        existingCharacter.setSheetIds(new ArrayList<>(request.getSheetIds()));
                    }
                    return characterRepository.save(existingCharacter);
                })
                .map(this::convertToResponse);
    }

    public Mono<Void> deleteCharacter(String id) {
        return characterRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Personagem não encontrado com ID: " + id)))
                .flatMap(character -> {
                    // Opcional: Lógica para deletar as fichas associadas no serviço Python.
                    // Esta é uma decisão de negócio: Se o personagem morre, suas fichas morrem com ele?
                    // Se sim, descomente e ajuste o código abaixo.
                    /*
                    Flux<Void> deleteSheetsFlux = Flux.fromIterable(character.getSheetIds())
                            .flatMap(sheetId -> sheetsWebClient.delete()
                                    .uri("/{sheetId}", sheetId) // Endpoint do Python para deletar ficha por ID
                                    .retrieve()
                                    .bodyToMono(Void.class)
                                    .onErrorResume(WebClientResponseException.class, e -> {
                                        System.err.println("Aviso: Erro ao deletar ficha " + sheetId + " do serviço Python: " + e.getMessage());
                                        return Mono.empty(); // Permite que a deleção do personagem continue mesmo com falha em uma ficha
                                    })
                            );
                    return deleteSheetsFlux.then(characterRepository.delete(character));
                    */
                    // Por padrão, apenas deleta o personagem e mantém as fichas no serviço Python
                    return characterRepository.delete(character);
                });
    }

    // --- Métodos de Interação com o Serviço Python de Fichas ---

    /**
     * Cria uma nova ficha no serviço Python de Fichas e associa seu ID ao personagem Java.
     * @param characterId O ID do personagem ao qual a ficha será associada.
     * @param createPayload Os dados para criar a ficha.
     * @return A ficha criada (retornada pelo serviço Python).
     */
    public Mono<Sheet> createSheetForCharacter(String characterId, SheetCreatePayload createPayload) {
        return characterRepository.findById(characterId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Personagem não encontrado para associar ficha: " + characterId)))
                .flatMap(character -> {
                    // Define o ownerId da ficha como o ID do personagem Java
                    createPayload.setOwnerId(character.getId());

                    return sheetsWebClient.post()
                            .uri("/") // Corresponde a /api/sheets/ no serviço Python
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(createPayload)
                            .retrieve()
                            .bodyToMono(Sheet.class) // Espera a resposta do tipo Sheet do Python
                            .flatMap(createdSheet -> {
                                // Adiciona o ID da ficha criada à lista do personagem e salva o personagem
                                character.getSheetIds().add(createdSheet.getId());
                                return characterRepository.save(character)
                                        .thenReturn(createdSheet); // Retorna a ficha criada
                            })
                            .onErrorMap(WebClientResponseException.class, e -> {
                                System.err.println("Erro ao criar ficha no serviço Python: " + e.getResponseBodyAsString());
                                // Mapeia o erro do WebClient para uma exceção de negócio
                                return new BadRequestException("Falha ao criar ficha no serviço externo: " + e.getResponseBodyAsString());
                            });
                });
    }

    /**
     * Busca todas as fichas associadas a um personagem no serviço Python de Fichas.
     * @param characterId O ID do personagem.
     * @return Um Flux de objetos Sheet (representando as fichas do serviço Python).
     */
    public Flux<Sheet> getSheetsByCharacterId(String characterId) {
        return characterRepository.findById(characterId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Personagem não encontrado para buscar fichas: " + characterId)))
                .flatMapMany(character ->
                        sheetsWebClient.get()
                                // O endpoint do Python é /api/sheets/by-user_id/{user_id}
                                .uri("/by-user_id/{ownerId}", character.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToFlux(Sheet.class) // Espera uma lista de Sheets do Python
                                .onErrorResume(WebClientResponseException.class, e -> {
                                    System.err.println("Erro ao buscar fichas para o personagem " + character.getId() + " no serviço Python: " + e.getResponseBodyAsString());
                                    return Flux.error(new BadRequestException("Falha ao buscar fichas no serviço externo: " + e.getResponseBodyAsString()));
                                })
                );
    }

    // Método auxiliar para converter entidade Character para DTO CharacterResponse
    private CharacterResponse convertToResponse(Character character) {
        CharacterResponse response = new CharacterResponse();
        response.setId(character.getId());
        response.setName(character.getName());
        response.setPlayerUserId(character.getPlayerUserId());
        response.setSheetIds(character.getSheetIds());
        return response;
    }
}
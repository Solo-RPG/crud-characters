package com.rpgsheets.characterservice.service;

import com.rpgsheets.characterservice.exception.BadRequestException;
import com.rpgsheets.characterservice.exception.ResourceNotFoundException;
import com.rpgsheets.characterservice.model.*;
import com.rpgsheets.characterservice.repository.CharacterSheetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono; // Necessário para trabalhar com Mono do WebClient

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

// Este serviço contém a lógica de negócio para as operações de CRUD das fichas de personagem.
@Service // Indica ao Spring que esta classe é um componente de serviço
public class CharacterSheetService {

    private final CharacterSheetRepository characterSheetRepository; // Para interação com o MongoDB
    private final TemplateService templateService; // Para buscar templates do serviço Python

    // Injeção de dependências via construtor
    public CharacterSheetService(CharacterSheetRepository characterSheetRepository, TemplateService templateService) {
        this.characterSheetRepository = characterSheetRepository;
        this.templateService = templateService;
    }

    /**
     * Cria uma nova ficha de personagem.
     * Envolve a busca do template e a validação dos dados fornecidos pelo usuário.
     * @param request Os dados da requisição para criar a ficha.
     * @return Um Mono que emitirá a CharacterSheetResponse da ficha criada.
     */
    public Mono<CharacterSheetResponse> createCharacterSheet(CharacterSheetRequest request) {
        // Validação inicial: garantir que um ID ou nome de template foi fornecido
        if (request.getTemplateId() == null && request.getSystemName() == null) {
            return Mono.error(new BadRequestException("Pelo menos um identificador (templateId ou systemName) deve ser fornecido para criar a ficha."));
        }

        // Determina qual método do TemplateService usar (por ID ou por nome do sistema)
        Mono<TemplateResponse> templateMono;
        if (request.getTemplateId() != null) {
            templateMono = templateService.getTemplateById(request.getTemplateId());
        } else {
            templateMono = templateService.getTemplateByName(request.getSystemName());
        }

        // `flatMap` é usado para encadear operações reativas (primeiro busca o template, depois cria a ficha)
        return templateMono.flatMap(template -> {
            try {
                // Constrói os dados da ficha validando-os contra a estrutura do template
                Map<String, SheetField> sheetData = buildSheetData(request.getFields(), template.getFields(), "");

                CharacterSheet characterSheet = new CharacterSheet();
                characterSheet.setId(UUID.randomUUID().toString()); // Gera um ID único para a nova ficha
                characterSheet.setOwnerId(request.getOwnerId());
                characterSheet.setTemplateId(template.getId());
                characterSheet.setTemplateSystemName(template.getSystemName());
                characterSheet.setTemplateSystemVersion(template.getVersion());
                characterSheet.setData(sheetData); // Define os dados da ficha validados

                // Salva a ficha no MongoDB e depois a converte para o DTO de resposta
                // `Mono.just` empacota um valor não reativo em um Mono.
                return Mono.just(characterSheetRepository.save(characterSheet))
                        .map(this::convertToResponse); // Chama um método auxiliar para converter
            } catch (IllegalArgumentException e) {
                // Captura exceções de validação de dados e as transforma em BadRequestException
                return Mono.error(new BadRequestException(e.getMessage()));
            }
        });
    }

    /**
     * Busca uma ficha de personagem pelo seu ID.
     * @param id O ID da ficha.
     * @return Um Mono que emitirá a CharacterSheetResponse, ou ResourceNotFoundException se a ficha não existir.
     */
    public Mono<CharacterSheetResponse> getCharacterSheetById(String id) {
        // `Mono.justOrEmpty` cria um Mono a partir de um Optional (retorno de findById)
        // `switchIfEmpty` é chamado se o Mono estiver vazio (ficha não encontrada)
        return Mono.justOrEmpty(characterSheetRepository.findById(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Ficha de personagem não encontrada com ID: " + id)))
                .map(this::convertToResponse);
    }

    /**
     * Retorna todas as fichas de personagem de forma paginada.
     * @param pageable Objeto Pageable contendo informações de paginação (número da página, tamanho, ordenação).
     * @return Um Mono que emitirá uma Page de CharacterSheetResponse.
     */
    public Mono<Page<CharacterSheetResponse>> getAllCharacterSheets(Pageable pageable) {
        // `Mono.fromCallable` é usado para operações bloqueantes (síncronas) dentro de um contexto reativo,
        // garantindo que elas sejam executadas em um thread apropriado.
        return Mono.fromCallable(() -> characterSheetRepository.findAll(pageable))
                .map(page -> new PageImpl<>( // Converte a Page de CharacterSheet para Page de CharacterSheetResponse
                        page.getContent().stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList()),
                        pageable,
                        page.getTotalElements()
                ));
    }

    /**
     * Atualiza uma ficha de personagem existente.
     * @param id O ID da ficha a ser atualizada.
     * @param request Os novos dados para a ficha.
     * @return Um Mono que emitirá a CharacterSheetResponse da ficha atualizada.
     */
    public Mono<CharacterSheetResponse> updateCharacterSheet(String id, CharacterSheetRequest request) {
        return Mono.justOrEmpty(characterSheetRepository.findById(id)) // Primeiro, busca a ficha existente
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Ficha de personagem não encontrada com ID: " + id)))
                .flatMap(existingSheet -> { // Se a ficha existir, prossegue com a atualização
                    Mono<TemplateResponse> templateMono;
                    if (request.getTemplateId() != null) {
                        templateMono = templateService.getTemplateById(request.getTemplateId());
                    } else if (request.getSystemName() != null) {
                        templateMono = templateService.getTemplateByName(request.getSystemName());
                    } else {
                        // Se nenhum ID ou nome de template for fornecido na requisição de atualização,
                        // usamos o template que já está associado à ficha existente para revalidar os campos.
                        templateMono = templateService.getTemplateById(existingSheet.getTemplateId());
                    }

                    return templateMono.flatMap(template -> {
                        try {
                            // Revalida todos os campos da requisição com base no template
                            Map<String, SheetField> updatedData = buildSheetData(request.getFields(), template.getFields(), "");

                            existingSheet.setOwnerId(request.getOwnerId()); // Atualiza o ownerId
                            existingSheet.setTemplateId(template.getId()); // Atualiza o ID do template
                            existingSheet.setTemplateSystemName(template.getSystemName()); // Atualiza o nome do sistema do template
                            existingSheet.setTemplateSystemVersion(template.getVersion()); // Atualiza a versão do template
                            existingSheet.setData(updatedData); // Atualiza os dados da ficha

                            return Mono.just(characterSheetRepository.save(existingSheet)) // Salva as alterações
                                    .map(this::convertToResponse);
                        } catch (IllegalArgumentException e) {
                            return Mono.error(new BadRequestException(e.getMessage()));
                        }
                    });
                });
    }

    /**
     * Exclui uma ficha de personagem pelo ID.
     * @param id O ID da ficha a ser excluída.
     * @return Um Mono<Void> indicando que a operação foi concluída (não retorna nada).
     */
    public Mono<Void> deleteCharacterSheet(String id) {
        // `Mono.fromRunnable` é usado para executar uma operação que não retorna um valor, mas pode ter efeitos colaterais.
        return Mono.fromRunnable(() -> characterSheetRepository.deleteById(id));
    }

    /**
     * Método auxiliar recursivo para construir e validar os dados da ficha
     * com base nas definições do template e nos dados do usuário.
     * @param userData Dados enviados pelo usuário para preencher a ficha.
     * @param templateFields Campos definidos no template para o nível atual.
     * @param parentPath Caminho dos campos pai para mensagens de erro mais claras.
     * @return Um mapa de SheetField, representando os dados da ficha validados e estruturados.
     */
    private Map<String, SheetField> buildSheetData(
            Map<String, Object> userData,
            List<TemplateField> templateFields,
            String parentPath
    ) {
        Map<String, SheetField> sheetData = new HashMap<>();
        for (TemplateField templateField : templateFields) {
            String fieldName = templateField.getName();
            // Constrói o caminho completo do campo para mensagens de erro
            String currentPath = parentPath.isEmpty() ? fieldName : parentPath + "." + fieldName;

            // Verifica se o campo obrigatório está faltando nos dados do usuário
            if (!userData.containsKey(fieldName)) {
                if (templateField.isRequired()) {
                    throw new IllegalArgumentException(String.format("Campo obrigatório faltando: %s", currentPath));
                }
                continue; // Ignora campos opcionais que não foram enviados
            }

            Object userValue = userData.get(fieldName);

            // Se o campo do template tem sub-campos, significa que é um objeto aninhado
            if (templateField.getFields() != null && !templateField.getFields().isEmpty()) {
                if (!(userValue instanceof Map)) { // Verifica se o valor fornecido é um mapa (objeto)
                    throw new IllegalArgumentException(String.format("Campo %s deve ser um objeto.", currentPath));
                }
                // Chama recursivamente para processar os campos aninhados
                Map<String, SheetField> nestedFields = buildSheetData((Map<String, Object>) userValue, templateField.getFields(), currentPath);
                sheetData.put(fieldName, new SheetField(nestedFields, templateField.isRequired(), templateField.getOptions()));
            } else if (templateField.getType() == FieldType.LIST) { // Se o campo do template é uma lista
                if (!(userValue instanceof List)) { // Verifica se o valor fornecido é uma lista
                    throw new IllegalArgumentException(String.format("Campo %s deve ser uma lista.", currentPath));
                }
                // Para listas, a validação interna dos itens da lista pode ser mais complexa.
                // Aqui, simplesmente armazenamos a lista. Se cada item da lista tiver uma estrutura específica,
                // o TemplateField precisaria de uma definição para o tipo dos itens da lista.
                sheetData.put(fieldName, new SheetField(userValue, templateField.isRequired(), templateField.getOptions()));
            } else { // É um campo simples (string, number, boolean)
                validateField(userValue, templateField, currentPath); // Valida o tipo e as opções do campo simples
                sheetData.put(fieldName, new SheetField(userValue, templateField.isRequired(), templateField.getOptions()));
            }
        }
        return sheetData;
    }

    /**
     * Método auxiliar para validar um campo simples contra as regras do template.
     * @param value O valor fornecido pelo usuário.
     * @param templateField A definição do campo no template.
     * @param fieldPath O caminho completo do campo para mensagens de erro.
     */
    private void validateField(Object value, TemplateField templateField, String fieldPath) {
        // Validação de tipo
        switch (templateField.getType()) {
            case STRING:
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException(String.format("Campo %s deve ser uma string.", fieldPath));
                }
                break;
            case NUMBER:
                // Permite Integer, Double ou Float para o tipo "number"
                if (!(value instanceof Integer || value instanceof Double || value instanceof Float)) {
                    throw new IllegalArgumentException(String.format("Campo %s deve ser um número.", fieldPath));
                }
                break;
            case BOOLEAN:
                if (!(value instanceof Boolean)) {
                    throw new IllegalArgumentException(String.format("Campo %s deve ser um booleano.", fieldPath));
                }
                break;
            // Tipos LIST e OBJECT são tratados no método buildSheetData (recursivamente)
            default:
                // Nenhum tratamento adicional necessário para outros tipos de campo aqui
                break;
        }

        // Validação de opções (se o template definir opções permitidas)
        if (templateField.getOptions() != null && !templateField.getOptions().isEmpty()) {
            // Converte o valor para String para comparar com a lista de opções
            if (!templateField.getOptions().contains(String.valueOf(value))) {
                throw new IllegalArgumentException(
                        String.format("Valor inválido para %s. Opções permitidas: %s", fieldPath, templateField.getOptions()));
            }
        }
    }

    /**
     * Método auxiliar para converter um objeto CharacterSheet (do banco) para
     * um CharacterSheetResponse (para a API).
     * @param characterSheet O objeto CharacterSheet a ser convertido.
     * @return Um objeto CharacterSheetResponse.
     */
    private CharacterSheetResponse convertToResponse(CharacterSheet characterSheet) {
        CharacterSheetResponse response = new CharacterSheetResponse();
        response.setId(characterSheet.getId());
        response.setTemplateId(characterSheet.getTemplateId());
        response.setTemplateSystemName(characterSheet.getTemplateSystemName());
        response.setTemplateSystemVersion(characterSheet.getTemplateSystemVersion());
        response.setOwnerId(characterSheet.getOwnerId());
        response.setData(characterSheet.getData());
        return response;
    }
}
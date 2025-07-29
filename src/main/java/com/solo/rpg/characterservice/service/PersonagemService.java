package com.solo.rpg.characterservice.service;

import com.solo.rpg.characterservice.model.Personagem;
import com.solo.rpg.characterservice.model.PersonagemCreateRequest; // Importe esta classe
import com.solo.rpg.characterservice.repository.PersonagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID; // Para gerar IDs únicos para o personagem

@Service // Indica que esta é uma classe de serviço e um componente Spring
public class PersonagemService {

    private final PersonagemRepository personagemRepository;

    // Injeção de dependências: Spring injeta a instância necessária do repositório
    @Autowired
    public PersonagemService(PersonagemRepository personagemRepository) {
        this.personagemRepository = personagemRepository;
        // Não precisamos de RestTemplate aqui se o Personagem apenas referencia a Ficha e não a cria diretamente
    }

    /**
     * Cria um novo personagem com base nos dados da requisição.
     * @param request DTO com ownerId, nomePersonagem e fichaId.
     * @return O objeto Personagem salvo no banco de dados.
     */
    public Personagem createPersonagem(PersonagemCreateRequest request) {
        // Validações básicas dos campos obrigatórios na requisição
        if (request.getOwnerId() == null || request.getOwnerId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ownerId é obrigatório.");
        }
        if (request.getNomePersonagem() == null || request.getNomePersonagem().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nomePersonagem é obrigatório.");
        }
        // Validação se fichaId é obrigatório para um novo personagem
        if (request.getFichaId() == null || request.getFichaId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fichaId é obrigatório para criar um personagem.");
        }

        Personagem novoPersonagem = new Personagem();
        novoPersonagem.setId(UUID.randomUUID().toString()); // Gera um ID único para este objeto Personagem
        novoPersonagem.setOwnerId(request.getOwnerId());
        novoPersonagem.setNomePersonagem(request.getNomePersonagem());
        novoPersonagem.setFichaId(request.getFichaId()); // Atribui o ID da ficha associada

        // Salva o novo personagem no MongoDB
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
     * @param id O ID do personagem a ser atualizado.
     * @param updateData Um mapa contendo os campos e novos valores para atualização.
     * @return O objeto Personagem atualizado, ou null se não for encontrado.
     */
    public Personagem updatePersonagem(String id, Map<String, Object> updateData) {
        Optional<Personagem> existingPersonagemOptional = personagemRepository.findById(id);

        if (existingPersonagemOptional.isEmpty()) {
            return null; // Personagem não encontrado
        }

        Personagem existingPersonagem = existingPersonagemOptional.get();

        // Atualiza campos permitidos se estiverem presentes no mapa updateData
        if (updateData.containsKey("ownerId")) {
            existingPersonagem.setOwnerId((String) updateData.get("ownerId"));
        }
        if (updateData.containsKey("nomePersonagem")) {
            existingPersonagem.setNomePersonagem((String) updateData.get("nomePersonagem"));
        }
        if (updateData.containsKey("fichaId")) {
            existingPersonagem.setFichaId((String) updateData.get("fichaId"));
        }
        // NOTA: O ID do personagem (existingPersonagem.getId()) não deve ser alterado.
        // O Lombok @Data e o Spring Data MongoDB já cuidam da persistência do ID.

        return personagemRepository.save(existingPersonagem); // Salva as alterações
    }

    /**
     * Deleta um personagem pelo seu ID único.
     * @param id O ID do personagem a ser deletado.
     * @return true se o personagem foi deletado com sucesso, false caso contrário (não encontrado).
     */
    public boolean deletePersonagem(String id) {
        // Verifica se o personagem existe antes de tentar deletar
        if (personagemRepository.existsById(id)) {
            personagemRepository.deleteById(id);
            return true;
        }
        return false; // Personagem não encontrado
    }
}
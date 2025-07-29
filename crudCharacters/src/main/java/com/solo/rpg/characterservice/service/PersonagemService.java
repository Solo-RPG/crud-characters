package com.solo.rpg.characterservice.service;

import com.solo.rpg.characterservice.model.Personagem;
import com.solo.rpg.characterservice.repository.PersonagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; // Para fazer requisições HTTP para o serviço de templates
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID; // Para gerar IDs como no Python

@Service // Indica que esta é uma classe de serviço e um componente Spring
public class PersonagemService {

    private final PersonagemRepository personagemRepository;
    private final RestTemplate restTemplate; // Cliente HTTP para comunicação com o serviço de templates

    // Injeção de dependências: Spring injeta as instâncias necessárias
    @Autowired
    public PersonagemService(PersonagemRepository personagemRepository) {
        this.personagemRepository = personagemRepository;
        this.restTemplate = new RestTemplate(); // Inicializa o RestTemplate
    }

    // Corresponde ao POST /api/sheets/ (create_sheet no Python)
    public Personagem createPersonagem(String ownerId, String templateId, String systemName, Map<String, Object> fields) {
        // 1. Busca do template
        // Você precisaria da URL do serviço de templates. Vamos simular aqui.
        // Em um cenário real, você injetaria essa URL via @Value("${templates.service.url}")
        // ou um objeto de configuração.
        String templatesServiceUrl = "http://localhost:8000/api/templates/"; // Ajuste conforme seu ambiente
        Map<String, Object> template = null;

        try {
            if (templateId != null && !templateId.isEmpty()) {
                template = restTemplate.getForObject(templatesServiceUrl + "by-id/" + templateId, Map.class);
            } else if (systemName != null && !systemName.isEmpty()) {
                template = restTemplate.getForObject(templatesServiceUrl + "by-name/" + systemName, Map.class);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pelo menos um identificador (templateId ou systemName) deve ser fornecido");
            }

            if (template == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Template não existe");
            }

        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Template não existe: " + e.getMessage());
        } catch (org.springframework.web.client.ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Serviço de templates indisponível");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao acessar o serviço de templates: " + e.getMessage());
        }


        // 2. Lógica de criação e validação (simplificada, pois o Python faz a validação detalhada)
        // No Python, você tem `create_sheet_from_template` e `_build_sheet_data`.
        // Para o Java, estamos assumindo que os `fields` já vêm pré-validados ou que a validação
        // de campo por campo (required, type, options) é responsabilidade do serviço de `templates`
        // e do frontend, como parece ser o caso no seu código Python, que faz a validação antes
        // de persistir a ficha.
        // Se você precisasse replicar a lógica _build_sheet_data aqui, seria mais complexo
        // com tipos dinâmicos (Map<String, Object> para representar o SheetField do Python).
        // Por simplicidade inicial, vamos persistir os `fields` como eles vêm.

        Personagem novoPersonagem = new Personagem();
        novoPersonagem.setId(UUID.randomUUID().toString()); // Gera um ID único, como o uuid.uuid4()
        novoPersonagem.setOwnerId(ownerId);
        novoPersonagem.setTemplateId(template.get("id").toString()); // Assume que o template tem um 'id'
        novoPersonagem.setTemplateSystemName(template.get("system_name").toString());
        novoPersonagem.setTemplateSystemVersion(template.get("version").toString());
        novoPersonagem.setData(fields); // Os dados brutos do personagem

        return personagemRepository.save(novoPersonagem); // Salva no MongoDB
    }

    // Corresponde ao GET /api/sheets/{sheet_id}
    public Optional<Personagem> getPersonagemById(String id) {
        return personagemRepository.findById(id);
    }

    // Corresponde ao GET /api/sheets/
    public List<Personagem> getAllPersonagens() {
        return personagemRepository.findAll();
    }

    // Corresponde ao GET /api/sheets/by-user_id/{user_id}
    public List<Personagem> getPersonagensByOwnerId(String ownerId) {
        return personagemRepository.findByOwnerId(ownerId);
    }

    // Corresponde ao PUT /api/sheets/{sheet_id}
    public Personagem updatePersonagem(String id, Map<String, Object> updateData) {
        Optional<Personagem> existingPersonagemOptional = personagemRepository.findById(id);

        if (existingPersonagemOptional.isEmpty()) {
            return null; // ou lançar uma exceção específica
        }

        Personagem existingPersonagem = existingPersonagemOptional.get();


        // Por exemplo, se updateData contiver um campo "nome", você faria:
        // if (updateData.containsKey("nome")) {
        //     existingPersonagem.getData().put("nome", updateData.get("nome"));
        // }
        // Para este exemplo, estamos substituindo o mapa `data` completamente
        // ou mesclando. A forma mais simples é atualizar o mapa `data` diretamente.
        // Se você precisar de uma atualização profunda (deep merge) de campos aninhados,
        // isso exigiria uma lógica mais complexa aqui.

        // Simplesmente substitui o campo 'data' ou mescla, dependendo da sua necessidade
        existingPersonagem.setData(updateData); // Isso substitui o mapa 'data' inteiro

        return personagemRepository.save(existingPersonagem); // Salva as alterações
    }

    // Corresponde ao DELETE /api/sheets/{sheet_id}
    public boolean deletePersonagem(String id) {
        // Verifica se o personagem existe antes de tentar deletar
        if (personagemRepository.existsById(id)) {
            personagemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
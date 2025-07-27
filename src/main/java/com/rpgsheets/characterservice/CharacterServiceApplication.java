package com.rpgsheets.characterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext; // Importe esta
import org.springframework.context.annotation.Bean;     // Importe esta
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.rpgsheets.characterservice.repository")
public class CharacterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CharacterServiceApplication.class, args);
    }

    // Adicione este Bean para verificar se o TemplateRepository é carregado
    @Bean
    public String checkTemplateRepository(ApplicationContext context) {
        try {
            TemplateRepository repo = context.getBean(TemplateRepository.class);
            System.out.println("✅ TemplateRepository bean carregado com sucesso!");
            return "TemplateRepository carregado";
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar TemplateRepository bean: " + e.getMessage());
            return "Erro ao carregar TemplateRepository";
        }
    }
}
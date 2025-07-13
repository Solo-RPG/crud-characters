package com.rpgsheets.characterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication 
@EnableMongoRepositories(basePackages = "com.rpgsheets.characterservice.repository") // Habilita a detecção e criação automática de instâncias dos repositórios Spring Data MongoDB.
public class CharacterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CharacterServiceApplication.class, args);
    }

}
package com.rpgsheets.characterservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    // URL do seu serviço de Fichas Python (CRUD de Fichas)
    @Value("${app.sheets-service-url}")
    private String sheetsServiceUrl;

    public String getSheetsServiceUrl() {
        return sheetsServiceUrl;
    }

    public void setSheetsServiceUrl(String sheetsServiceUrl) {
        this.sheetsServiceUrl = sheetsServiceUrl;
    }
}
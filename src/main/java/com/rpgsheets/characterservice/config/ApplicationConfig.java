package com.rpgsheets.characterservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration // classe configurações
public class ApplicationConfig {

    @Value("${app.templates-service-url}") // Injeta o valor da propriedade do application.yml
    private String templatesServiceUrl;

    // Getters e Setters para templatesServiceUrl
    public String getTemplatesServiceUrl() {
        return templatesServiceUrl;
    }

    public void setTemplatesServiceUrl(String templatesServiceUrl) {
        this.templatesServiceUrl = templatesServiceUrl;
    }
}
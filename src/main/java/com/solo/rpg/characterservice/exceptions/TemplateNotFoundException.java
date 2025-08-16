package com.solo.rpg.characterservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TemplateNotFoundException extends ResponseStatusException {
    public TemplateNotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}

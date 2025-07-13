package com.rpgsheets.characterservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exceção personalizada para quando um recurso (ficha) não é encontrado.
// A anotação @ResponseStatus fará com que o Spring retorne um status HTTP 404 (Not Found)
// quando esta exceção for lançada.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
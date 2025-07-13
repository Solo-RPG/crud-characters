package com.rpgsheets.characterservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exceção personalizada para requisições mal formatadas ou com dados inválidos.
// O Spring retornará um status HTTP 400 (Bad Request) quando esta exceção for lançada.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
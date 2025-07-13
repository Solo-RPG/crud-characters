package com.rpgsheets.characterservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;

// @ControllerAdvice é uma anotação que permite lidar com exceções em todo o aplicativo,
// centralizando o tratamento de erros.
@ControllerAdvice
public class GlobalExceptionHandler {

    // Lida com a ResourceNotFoundException, retornando 404 Not Found.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Not Found");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Lida com a BadRequestException, retornando 400 Bad Request.
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Bad Request");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Lida com exceções de validação de argumentos de método (ex: campos obrigatórios ausentes ou inválidos).
    // Usado com @Valid em DTOs.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())); // Coleta mensagens de erro por campo
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Lida com erros que ocorrem ao se comunicar com outros serviços (como o serviço de templates).
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, String>> handleWebClientResponseException(WebClientResponseException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Service Communication Error");
        error.put("message", "Erro ao comunicar com o serviço externo: " + ex.getStatusText() + " - " + ex.getResponseBodyAsString());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Manipulador genérico
    // Garante que a API sempre retorne uma resposta formatada em caso de erro inesperado.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", "Ocorreu um erro inesperado: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
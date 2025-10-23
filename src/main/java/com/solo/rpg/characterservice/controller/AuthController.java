package com.solo.rpg.characterservice.controller;
import com.solo.rpg.characterservice.model.User;
import com.solo.rpg.characterservice.service.AuthService;
import com.solo.rpg.characterservice.model.AuthResponse;
import com.solo.rpg.characterservice.model.LoginRequest;
import com.solo.rpg.characterservice.model.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Registrar um novo usuário")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
            return ResponseEntity.badRequest().build(); // ou retornar um ErrorResponse
        }
    }

    @Operation(summary = "Logar no sistema")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
package com.solo.rpg.characterservice.controller;

import com.solo.rpg.characterservice.model.User;
import com.solo.rpg.characterservice.repository.UserRepository;
import com.solo.rpg.characterservice.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuário", description = "Operações relacionadas ao usuário")
public class UserController {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserController(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Obter usuário atual")
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String email = tokenService.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(user);
    }
}
package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.MeResponseDTO;
import com.breno.marketplace_test.dtos.UserRequestDTO;
import com.breno.marketplace_test.dtos.UserResponseDTO;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.security.SecurityUser;
import com.breno.marketplace_test.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> listUsers() {
        return ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequestDTO user) {
        log.info("Requisição POST para criar novo usuário com email: {}", user.email());
        userService.saveUser(user);
        log.info("Usuário criado com sucesso: {}", user.email());
        return ok("User created successfully!");
    }

    @GetMapping("/me")
    // 2. ABORDAGEM À PROVA DE BALA: Usar a interface Authentication do Spring
    public ResponseEntity<MeResponseDTO> getMe(Authentication authentication) {

        // 3. O getName() devolve o identificador principal (geralmente o email ou username)
        // Independentemente do que o JwtFilter guardou, isto vai funcionar!
        String emailDoUsuario = authentication.getName();

        // 4. Usamos a tua lógica que já estava perfeita para buscar o DTO
        MeResponseDTO dto = userService.findByEmail(emailDoUsuario);

        // 5. Retornamos com sucesso
        return ResponseEntity.ok(dto);
    }
}

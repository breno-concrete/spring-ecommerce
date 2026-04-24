package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.UserRequestDTO;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/v1/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return ok(userService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<String> criarUsuario(@RequestBody UserRequestDTO user) {
        userService.saveUser(user);
        // Lógica para criar um novo usuário
        return ok("Usuário criado com sucesso!");
    }
}

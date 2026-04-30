package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.UserRequestDTO;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> listUsers() {
        return ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO user) {
        userService.saveUser(user);
        return ok("User created successfully!");
    }
}

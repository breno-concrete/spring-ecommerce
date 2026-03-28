package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/usuarios")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping
    public 

}

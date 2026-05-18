package com.breno.marketplace_test.controllers;


import com.breno.marketplace_test.dtos.*;
import com.breno.marketplace_test.security.JwtTokenProvider;
import com.breno.marketplace_test.services.AuthService;
import com.breno.marketplace_test.services.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService service;

        @PostMapping("/login")
        public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto){
            log.info("Requisição de login recebida para email: {}", dto.email());
            String accessToken = service.login(dto);
            String refreshToken = jwtTokenProvider.generateRefreshToken(dto.email());
            log.info("Login bem-sucedido para o email: {}", dto.email());
            return ResponseEntity.ok(new LoginResponseDTO(accessToken, refreshToken, dto.email()));
        }

        @PostMapping("/register")
        public ResponseEntity<Void> register(@RequestBody @Valid UserRequestDTO dto){
            log.info("Requisição de registro recebida para email: {}", dto.email());
            service.register(dto);
            log.info("Registro bem-sucedido para o email: {}", dto.email());
            return ResponseEntity.ok().build();
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(
                @RequestHeader("Authorization") String tokenHeader){
            log.info("Requisição de logout recebida");
            service.logout(tokenHeader);
            log.info("Logout bem-sucedido");

            // O token é enviado no header Authorization, então não precisamos de um corpo para logout
            // O serviço de logout irá extrair o token do header e invalidá-lo
            return ResponseEntity.noContent().build();

        }

        @PostMapping("/refresh")
        public ResponseEntity<RefreshTokenResponseDTO> refreshToken(
                @Valid @RequestBody RefreshTokenRequestDTO requestDTO){
            log.info("Requisição de refresh token recebida");
            RefreshTokenResponseDTO newToken = service.refreshToken(requestDTO);
            log.info("Refresh token bem-sucedido");
            return ResponseEntity.ok(newToken);
        }

}

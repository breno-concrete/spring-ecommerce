package com.breno.marketplace_test.controllers;


import com.breno.marketplace_test.dtos.*;
import com.breno.marketplace_test.security.JwtTokenProvider;
import com.breno.marketplace_test.services.AuthService;
import com.breno.marketplace_test.services.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService service;

        @PostMapping("/login")
        public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto){
            String accessToken = service.login(dto);
            String refreshToken = jwtTokenProvider.generateRefreshToken(dto.email());
            return ResponseEntity.ok(new LoginResponseDTO(accessToken, refreshToken, dto.email()));
        }

        @PostMapping("/register")
        public ResponseEntity<Void> register(@RequestBody @Valid UserRequestDTO dto){
            service.register(dto);
            return ResponseEntity.ok().build();
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(
                @RequestHeader("Authorization") String tokenHeader){
            service.logout(tokenHeader);

            // O token é enviado no header Authorization, então não precisamos de um corpo para logout
            // O serviço de logout irá extrair o token do header e invalidá-lo
            return ResponseEntity.noContent().build();

        }

        @PostMapping("/refresh")
        public ResponseEntity<RefreshTokenResponseDTO> refreshToken(
                @Valid @RequestBody RefreshTokenRequestDTO requestDTO){
            RefreshTokenResponseDTO newToken = service.refreshToken(requestDTO);
            return ResponseEntity.ok(newToken);
        }

}

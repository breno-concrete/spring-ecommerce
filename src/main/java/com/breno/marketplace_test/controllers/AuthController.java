package com.breno.marketplace_test.controllers;


import com.breno.marketplace_test.dtos.*;
import com.breno.marketplace_test.security.JwtTokenProvider;
import com.breno.marketplace_test.services.AuthService;
import com.breno.marketplace_test.services.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints de autenticação, registro e gerenciamento de sessões")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService service;



        @PostMapping("/login")
        @Operation(
                summary = "Autenticar usuário (Login)",
                description = "Realiza a autenticação utilizando email e senha. Retorna um token de acesso (JWT) e um refresh token para manter a sessão."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
                @ApiResponse(responseCode = "401", description = "Credenciais incorretas (Não autorizado)"),
                @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        })
        public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto){
            log.info("Requisição de login recebida para email: {}", dto.email());
            String accessToken = service.login(dto);
            String refreshToken = jwtTokenProvider.generateRefreshToken(dto.email());
            log.info("Login bem-sucedido para o email: {}", dto.email());
            return ResponseEntity.ok(new LoginResponseDTO(accessToken, refreshToken, dto.email()));
        }

        @PostMapping("/register")
        @Operation(
                summary = "Cadastrar novo usuário",
                description = "Cria uma nova conta de usuário no sistema com os dados fornecidos. Retorna status 201 (Created) em caso de sucesso."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (ex: senha fraca, formato de email incorreto)"),
                @ApiResponse(responseCode = "409", description = "Conflito: Email já está cadastrado no sistema")
        })
        public ResponseEntity<Void> register(@RequestBody @Valid UserRequestDTO dto){
            log.info("Requisição de registro recebida para email: {}", dto.email());
            service.register(dto);
            log.info("Registro bem-sucedido para o email: {}", dto.email());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        @PostMapping("/logout")
        @Operation(
                summary = "Realizar logout",
                description = "Recebe o token atual pelo header 'Authorization' e o invalida, encerrando a sessão de forma segura."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso (No Content)"),
                @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado")
        })
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
        @Operation(
                summary = "Atualizar token de acesso",
                description = "Recebe um refresh token válido e gera um novo token de acesso (JWT) para que o usuário não precise fazer login novamente."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Novo token gerado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Formato de requisição inválido"),
                @ApiResponse(responseCode = "401", description = "Refresh token inválido, expirado ou revogado")
        })
        public ResponseEntity<RefreshTokenResponseDTO> refreshToken(
                @Valid @RequestBody RefreshTokenRequestDTO requestDTO){
            log.info("Requisição de refresh token recebida");
            RefreshTokenResponseDTO newToken = service.refreshToken(requestDTO);
            log.info("Refresh token bem-sucedido");
            return ResponseEntity.ok(newToken);
        }

}

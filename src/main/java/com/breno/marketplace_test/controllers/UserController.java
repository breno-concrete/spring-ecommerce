package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.MeResponseDTO;
import com.breno.marketplace_test.dtos.UserRequestDTO;
import com.breno.marketplace_test.dtos.UserResponseDTO;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.security.SecurityUser;
import com.breno.marketplace_test.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Operações administrativas e perfis de usuários")
public class UserController {

    private final UserService userService;



    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna uma lista paginada de todos os usuários cadastrados no sistema. Requer privilégios de administrador (Role: ADMIN)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Requer privilégios de Administrador)")
    })
    public ResponseEntity<Page<UserResponseDTO>> listUsers(
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {

        Page<UserResponseDTO> page = userService.findAll(pageable);

        return ResponseEntity.ok(page);
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar novo usuário",
            description = "Cria uma nova conta de usuário no marketplace validando os dados de entrada. Retorna status 201 (Created) em caso de sucesso."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (ex: senha fora do padrão, campos vazios)"),
            @ApiResponse(responseCode = "409", description = "Conflito: Email já está cadastrado no sistema")
    })
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequestDTO user) {
        log.info("Requisição POST para criar novo usuário com email: {}", user.email());
        userService.saveUser(user);
        log.info("Usuário criado com sucesso: {}", user.email());
        return ResponseEntity.status(201).body("Usuário criado com sucesso: " + user.email());
    }

    @GetMapping("/me")
    @Operation(
            summary = "Buscar perfil logado (Me)",
            description = "Lê o token JWT da requisição e retorna os detalhes e informações de perfil do próprio usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil do usuário retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente, inválido ou expirado)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado no banco de dados")
    })
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

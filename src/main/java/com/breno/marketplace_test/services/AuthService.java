package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.LoginRequestDTO;
import com.breno.marketplace_test.dtos.RefreshTokenRequestDTO;
import com.breno.marketplace_test.dtos.RefreshTokenResponseDTO;
import com.breno.marketplace_test.dtos.UserRequestDTO;
import com.breno.marketplace_test.enums.UserRole;
import com.breno.marketplace_test.exceptions.InvalidTokenException;
import com.breno.marketplace_test.exceptions.UserAlreadyExistsException;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.repositories.UserRepository;
import com.breno.marketplace_test.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;

    @Transactional
    public String login(LoginRequestDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password()) // Use dto.password() ou dto.password() dependendo do seu DTO
        );

        log.info("User " + auth.getName() + " authenticated successfully.");
        return jwtTokenProvider.generateToken(auth.getName());


    }

    @Transactional
    public void register(UserRequestDTO dto){
        if(userRepository.existsByEmail(dto.email())){

            log.warn("Tentativa de registro falhou. O email '{}' já está em uso.", dto.email());

            throw new UserAlreadyExistsException(dto.email());
        }

        User user = User.builder()
                .fullName(dto.fullName())
                .email(dto.email())
                .phone(dto.phone())
                .role(UserRole.USER)
                .passwordHash(passwordEncoder.encode(dto.password()))
                .build();
        userRepository.save(user);

        log.info("Novo usuário registrado com sucesso: {}", dto.email());

    }

    public void logout(String tokenHeader){
        if(tokenHeader == null || !tokenHeader.startsWith("Bearer ")){
            throw new InvalidTokenException("No token or broken token");
        }


        String token = tokenHeader.substring(7); // tira o Bearer
        long ttl = jwtTokenProvider.getRemainingTtlMillis(token); // pega o tempo que falta

        if(ttl <= 0) return; //  se for negativo ele ja retorna (TOken ja expirou)

        if(tokenBlacklistService.isBlacklisted(token)){ //se ta na lista
            throw new InvalidTokenException("Token is already revoke");
        }

        tokenBlacklistService.blacklist(token, ttl); // adiciona no bacno Redis

    }

    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO request){
        String requestRefreshToken = request.refreshToken();

        String email = jwtTokenProvider.validateRefreshTokenAndGetEmail(requestRefreshToken); // valida o token e pega o email

        String newAccessToken = jwtTokenProvider.generateToken(email); // gera um novo token de acesso
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email); // gera um novo token

        return new RefreshTokenResponseDTO(newAccessToken, newRefreshToken);
    }
}
package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.LoginRequestDTO;
import com.breno.marketplace_test.dtos.UserRequestDTO;
import com.breno.marketplace_test.enums.UserRole;
import com.breno.marketplace_test.exceptions.InvalidTokenException;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.repositories.UserRepository;
import com.breno.marketplace_test.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        return jwtTokenProvider.generateToken(auth.getName());
    }

    @Transactional
    public void register(UserRequestDTO dto){
        if(userRepository.existsByEmail(dto.email())){
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .fullName(dto.fullName())
                .email(dto.email())
                .phone(dto.phone())
                .role(UserRole.USER)
                .passwordHash(passwordEncoder.encode(dto.password()))
                .build();
        userRepository.save(user);

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
}
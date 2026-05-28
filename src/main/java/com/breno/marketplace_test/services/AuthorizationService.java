package com.breno.marketplace_test.services;

import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.repositories.UserRepository;
import com.breno.marketplace_test.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Carregando detalhes do usuário com username/email: {}", username);
        // 1. Busca o usuário no banco.
        // Como o repositório agora retorna Optional, usamos o .orElseThrow
        User user = repository.findByEmail(username)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado com email: {}", username);
                    return new UsernameNotFoundException("User not found with email: " + username);
                });

        log.info("Usuário encontrado: {} - Role: {}", user.getEmail(), user.getRole());
        // 2. Agora sim: Instancia o SecurityUser passando o usuário encontrado
        return new SecurityUser(user);
    }
}
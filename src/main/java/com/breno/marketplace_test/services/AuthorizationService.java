package com.breno.marketplace_test.services;

import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.repositories.UserRepository;
import com.breno.marketplace_test.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Busca o usuário no banco.
        // Como o repositório agora retorna Optional, usamos o .orElseThrow
        User user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // 2. Agora sim: Instancia o SecurityUser passando o usuário encontrado
        return new SecurityUser(user);
    }
}
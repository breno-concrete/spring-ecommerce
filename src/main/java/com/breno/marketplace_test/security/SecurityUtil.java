package com.breno.marketplace_test.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public static Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Nenhum usuário autenticado no contexto atual");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof SecurityUser securityUser) {
            return securityUser.getId();
        }

        if (principal instanceof String username) {
            throw new IllegalStateException("Principal é string: " + username + " — use UserDetails customizado");
        }

        throw new IllegalStateException("Tipo de principal desconhecido: " + principal.getClass());
    }

}

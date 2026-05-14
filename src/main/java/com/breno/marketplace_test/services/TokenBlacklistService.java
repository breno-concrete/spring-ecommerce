package com.breno.marketplace_test.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private static final String BLACKLIST_PREFIX = "blacklist:"; //coloca prefixo em todos os tokens (identificação)

    private final RedisTemplate<String, String> redisTemplate; // falar com o banco

    public void blacklist(String token, long ttlMillis) { //ttlMillis Time to live em milissegundos
        String key = BLACKLIST_PREFIX + token; // junta o prefixo com o token para criar a chave única
        redisTemplate.opsForValue().set(key, "revoked", ttlMillis, TimeUnit.MILLISECONDS); //guarda o valor no Redis e diz em quanto tempo ele deve ser apagado
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token)); //verifica se o token está na lista negraJ
    }
}

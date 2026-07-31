package com.breno.marketplace_test.config;


import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)); // padrão de configuração, caso "esqueça" de apagar ele apaga em uma hora

        Map<String, RedisCacheConfiguration> specificConfigs = new HashMap<>();  // Existem regras diferentes para cada cache, então criamos um mapa para armazenar essas regras

        specificConfigs.put("token-blacklist", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(24)));

        specificConfigs.put("categories", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))); // outras gavetas de cache para otimizar busca no banco de dados

        specificConfigs.put("products", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)));

        specificConfigs.put("users-details", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig) // Passa o padrão
                .withInitialCacheConfigurations(specificConfigs) // Passa as regras específicas
                .build(); // Finaliza a criação
        //esse return é o manual de instruções para usar o redis cache, toda vez que for chamado, ele vem uso isso aqui com regra

    }


}


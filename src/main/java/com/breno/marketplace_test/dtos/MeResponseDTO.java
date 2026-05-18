package com.breno.marketplace_test.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

// MeResponseDTO.java
public record MeResponseDTO(
        long id,
        String name,
        String email,
        String role
) {}
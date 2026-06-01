package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryResponseDTO(

        Long id,

        String name
) {}

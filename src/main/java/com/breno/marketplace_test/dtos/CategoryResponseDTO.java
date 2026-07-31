package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record CategoryResponseDTO(

        Long id,

        String name
) implements Serializable {}

package com.breno.marketplace_test.dtos;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ErrorResponseDTO(

        String error,

        String message,

        LocalDateTime timestamp
) {}
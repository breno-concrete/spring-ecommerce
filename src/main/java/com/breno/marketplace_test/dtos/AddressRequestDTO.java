package com.breno.marketplace_test.dtos;

import jakarta.validation.constraints.*;

public record AddressRequestDTO(
        @NotBlank(message = "ZIP code is required")
        @Pattern(
                regexp = "^\\d{5}-?\\d{3}$",
                message = "Zip code must be in the format XXXXX-XXX or XXXXXXXX"
        )
        String zipCode,

        @NotBlank(message = "Street is required")
        @Size(min = 3, max = 255, message = "Street must be between 3 and 255 characters")
        String street,

        @NotBlank(message = "Number is required")
        @Size(min = 1, max = 20, message = "Number must be between 1 and 20 characters")
        String number,

        @Size(max = 255, message = "Complement can have a maximum of 255 characters")
        String complement,

        @NotBlank(message = "Neighborhood is required")
        @Size(min = 2, max = 100, message = "Neighborhood must be between 2 and 100 characters")
        String neighborhood,

        @NotBlank(message = "City is required")
        @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
        String city,

        @NotBlank(message = "State is required")
        @Size(min = 2, max = 2, message = "State must be the 2-letter UF acronym (e.g., DF, SP)")
        String state,

        @NotNull(message = "User ID is required")
        @Positive(message = "User ID must be a positive number")
        Long userId
) {}


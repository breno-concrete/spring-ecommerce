package com.breno.marketplace_test.dtos;

public record AddressResponseDTO(
        Long id,
        String zipCode,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        Long userId
) {}


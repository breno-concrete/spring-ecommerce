package com.breno.marketplace_test.mappers;

import com.breno.marketplace_test.dtos.AddressRequestDTO;
import com.breno.marketplace_test.dtos.AddressResponseDTO;
import com.breno.marketplace_test.models.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") // Avisa ao MapStruct que isso vai virar um @Component do Spring
public interface AddressMapper {

    // Só de escrever isso, a mágica acontece por baixo dos panos!
    AddressResponseDTO toDTO(Address address);

    Address toEntity(AddressRequestDTO addressDTO);

    List<AddressResponseDTO> toDTOList(List<Address> addresses);
}
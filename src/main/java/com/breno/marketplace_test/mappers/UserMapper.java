package com.breno.marketplace_test.mappers;

import com.breno.marketplace_test.dtos.UserRequestDTO;
import com.breno.marketplace_test.dtos.UserResponseDTO;
import com.breno.marketplace_test.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDTO userRequestDTO);
    UserResponseDTO toDTO(User user);
}

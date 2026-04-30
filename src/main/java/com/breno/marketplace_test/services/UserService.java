package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.UserRequestDTO;
import com.breno.marketplace_test.dtos.UserResponseDTO;
import com.breno.marketplace_test.exceptions.UserAlreadyExistsException;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.enums.UserRole;
import com.breno.marketplace_test.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO saveUser(UserRequestDTO dto){
        if(userRepository.existsByEmail(dto.email())){
            throw new UserAlreadyExistsException(dto.email());
        }
        User user = User.builder()
                .fullName(dto.fullName())
                .email(dto.email())
                .phone(dto.phone())
                .role(UserRole.USER)
                .passwordHash(passwordEncoder.encode(dto.password()))
                .build();

        return toResponseDTO(userRepository.save(user));
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public boolean deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }

}

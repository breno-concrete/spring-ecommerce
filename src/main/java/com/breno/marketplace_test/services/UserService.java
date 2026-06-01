package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.MeResponseDTO;
import com.breno.marketplace_test.dtos.UserRequestDTO;
import com.breno.marketplace_test.dtos.UserResponseDTO;
import com.breno.marketplace_test.exceptions.UserAlreadyExistsException;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.enums.UserRole;
import com.breno.marketplace_test.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;
    //precisa de final pois o @RequiredArgsConstructor vai reocnehcer ele e colocar no construtor
    // Previne Bugs

    @Transactional
    public UserResponseDTO saveUser(UserRequestDTO dto){
        log.info("Tentando salvar novo usuário com email: {}", dto.email());
        if(userRepository.existsByEmail(dto.email())){
            log.warn("Tentativa de salvar usuário falhou. O email '{}' já está em uso.", dto.email());
            throw new UserAlreadyExistsException(dto.email());
        }
        User user = User.builder()
                .fullName(dto.fullName())
                .email(dto.email())
                .phone(dto.phone())
                .role(UserRole.USER)
                .passwordHash(passwordEncoder.encode(dto.password()))
                .build();

        User savedUser = userRepository.save(user);
        log.info("Novo usuário registrado com sucesso: {} (ID: {})", dto.email(), savedUser.getId());
        return toResponseDTO(savedUser);
    }

    public List<UserResponseDTO> findAll(){
        return userRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto){
        log.info("Atualizando usuário com ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado para atualização", id);
                    return new IllegalStateException(id + " not found!");
                });

        user.setFullName(dto.fullName());
        user.setPhone(dto.phone());
        if(!user.getEmail().equals(dto.email())){
            if(userRepository.existsByEmail(dto.email())){
                log.warn("Tentativa de atualizar email para {} falhou. Email já em uso.", dto.email());
                throw new UserAlreadyExistsException(dto.email());
            }
            user.setEmail(dto.email());
            log.info("Email do usuário {} atualizado de {} para {}", id, user.getEmail(), dto.email());
        }
        if(dto.password() != null && !dto.password().isEmpty()){
            user.setPasswordHash(passwordEncoder.encode(dto.password()));
            log.info("Senha do usuário {} foi atualizada", id);
        }

        User updatedUser = userRepository.save(user);
        log.info("Usuário com ID {} atualizado com sucesso", id);
        return toResponseDTO(updatedUser);
    }

    public void deleteUser(Long id){
        log.info("Deletando usuário com ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado para deleção", id);
                    return new IllegalStateException(id + " not found!");
                });
        userRepository.delete(user);
        log.info("Usuário com ID {} deletado com sucesso", id);
    }

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );
    }

    public MeResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email " + email + " not found!"));

        return new MeResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}

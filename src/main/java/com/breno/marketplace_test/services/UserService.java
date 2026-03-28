package com.breno.marketplace_test.services;

import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(User newUser){
        return userRepository.save(newUser);
    }

    public List<User> listarTodos(){
        return userRepository.findAll();
    }

    public Optional<User> buscarPorId(Long id){
        return userRepository.findById(id);
    }

    public boolean deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

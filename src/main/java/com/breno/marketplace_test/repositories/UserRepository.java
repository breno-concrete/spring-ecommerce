package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);
}

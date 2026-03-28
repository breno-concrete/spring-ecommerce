package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}


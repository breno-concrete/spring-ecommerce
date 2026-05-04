package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}


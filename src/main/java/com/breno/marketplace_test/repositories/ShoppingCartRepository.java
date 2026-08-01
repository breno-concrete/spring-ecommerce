package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<ShoppingCart> findByUserId(Long userId);
}



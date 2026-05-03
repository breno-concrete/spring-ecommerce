package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}


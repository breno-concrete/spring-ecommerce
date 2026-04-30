package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}


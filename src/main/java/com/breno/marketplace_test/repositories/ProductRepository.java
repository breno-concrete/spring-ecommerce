package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @EntityGraph(attributePaths = {"category", "images"})
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"images", "category"})
    Optional<Product> findById(Long id);
}




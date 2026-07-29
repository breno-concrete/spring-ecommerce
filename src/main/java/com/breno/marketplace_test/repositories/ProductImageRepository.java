package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    Page<ProductImage> findByProductId(Pageable pageable, Long productId);
    List<ProductImage> findByProductId(Long productId);
}


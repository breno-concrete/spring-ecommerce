package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}


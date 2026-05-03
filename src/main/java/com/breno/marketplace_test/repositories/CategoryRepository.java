package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}


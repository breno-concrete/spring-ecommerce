package com.breno.marketplace_test.repositories;

import com.breno.marketplace_test.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosRepository extends JpaRepository<Produto, Integer> {

}

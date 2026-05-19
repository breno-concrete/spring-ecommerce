package com.breno.marketplace_test.services;

import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProdutoService {

    private final ProductRepository produtosRepository;

    public ProdutoService(ProductRepository produtosRepository) {
        this.produtosRepository = produtosRepository;
    }

    public List<Product> getProdutos(){
        return produtosRepository.findAll();
    }

    public void insertProduto(Product produto) {
        log.info("Inserindo novo produto: {}", produto.getName());
        Product savedProduct = produtosRepository.save(produto);
        log.info("Produto inserido com sucesso. ID: {}, Nome: {}", savedProduct.getId(), savedProduct.getName());
    }

    public Product getProdutosById(Long id) {
        return produtosRepository.findById(id).orElseThrow(() -> new IllegalStateException(id + " not found!") );

    }
}

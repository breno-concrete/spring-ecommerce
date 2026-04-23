package com.breno.marketplace_test.services;

import com.breno.marketplace_test.repositories.ProdutosRepository;
import com.breno.marketplace_test.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutosRepository produtosRepository;

    public ProdutoService(ProdutosRepository produtosRepository) {
        this.produtosRepository = produtosRepository;
    }

    public List<Product> getProdutos(){
        return produtosRepository.findAll();
    }

    public void insertProduto(Product produto) {
        produtosRepository.save(produto);
    }

    public Product getProdutosById(Integer id) {
        return produtosRepository.findById(id).orElseThrow(() -> new IllegalStateException(id + " not found!") );

    }
}

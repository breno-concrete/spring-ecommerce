package com.breno.marketplace_test.services;

import com.breno.marketplace_test.repositories.ProdutosRepository;
import com.breno.marketplace_test.models.Produto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutosRepository produtosRepository;

    public ProdutoService(ProdutosRepository produtosRepository) {
        this.produtosRepository = produtosRepository;
    }

    public List<Produto> getProdutos(){
        return produtosRepository.findAll();
    }

    public void insertProduto(Produto produto) {
        produtosRepository.save(produto);
    }

    public Produto getProdutosById(Integer id) {
        return produtosRepository.findById(id).orElseThrow(() -> new IllegalStateException(id + " not found!") );

    }
}

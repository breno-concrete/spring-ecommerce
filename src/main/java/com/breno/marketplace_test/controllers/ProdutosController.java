package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.services.ProdutoService;
import com.breno.marketplace_test.models.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/produtos")
public class ProdutosController {

    private final ProdutoService produtoService;

    public ProdutosController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<Product> getProdutos(){
        return produtoService.getProdutos();
    }

    @PostMapping
    public void addNewProduct(@RequestBody Product produto){
        produtoService.insertProduto(produto);
    }

    @GetMapping("{id}")
    public Product getProdutosById(@PathVariable Integer id){
        return produtoService.getProdutosById(id);
    }
}

package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.services.ProdutoService;
import com.breno.marketplace_test.models.Produto;
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
    public List<Produto> getProdutos(){
        return produtoService.getProdutos();
    }

    @PostMapping
    public void addNewProduct(@RequestBody Produto produto){
        produtoService.insertProduto(produto);
    }

    @GetMapping("{id}")
    public Produto getProdutosById(@PathVariable Integer id){
        return produtoService.getProdutosById(id);
    }
}

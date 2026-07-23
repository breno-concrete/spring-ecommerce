package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.ProductResponseDTO;
import com.breno.marketplace_test.dtos.ShoppingCartRequestDTO;
import com.breno.marketplace_test.dtos.ShoppingCartResponseDTO;
import com.breno.marketplace_test.security.SecurityUtil;
import com.breno.marketplace_test.services.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ResponseEntity<ShoppingCartResponseDTO> getCart(){
        long userId = SecurityUtil.getCurrentUserId();

        ShoppingCartResponseDTO cart = shoppingCartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);

    }

    @PostMapping("/items")
    public ResponseEntity<ShoppingCartResponseDTO> addItemToCart(@Valid  @RequestBody ShoppingCartRequestDTO cartRequest){
        Long userId = SecurityUtil.getCurrentUserId();
        log.info("Requisição POST para criar novo carrinho: {}", cartRequest.items());

        ShoppingCartResponseDTO response = shoppingCartService.addItemToCart(userId, itemRequest);

        return ResponseEntity.ok(response);

    }


    @PutMapping("/items/{itemId}")
    public ResponseEntity<ShoppingCartResponseDTO> updateCartItem(@PathVariable Long itemId, @RequestBody ProductResponseDTO itemRequest) {
        log.info("Requisição PUT para atualizar item do carrinho. Item ID: {}, Novo Produto ID: {}", itemId, itemRequest.id());

        ShoppingCartResponseDTO response = shoppingCartService.updateCartItem(itemId, itemRequest);

        return ResponseEntity.ok(response);
    }

     @DeleteMapping("/items/{itemId}")

}

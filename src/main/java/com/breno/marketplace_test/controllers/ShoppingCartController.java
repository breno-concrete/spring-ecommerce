package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.*;
import com.breno.marketplace_test.security.SecurityUtil;
import com.breno.marketplace_test.services.ShoppingCartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Operações do carrinho de compras do usuário")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ResponseEntity<ShoppingCartResponseDTO> getCart(){
        long userId = SecurityUtil.getCurrentUserId();

        ShoppingCartResponseDTO cart = shoppingCartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);

    }

    @PostMapping("/items")
    public ResponseEntity<ShoppingCartResponseDTO> addItemToCart(@Valid  @RequestBody CartItemDTO cartRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        log.info("Requisição POST para adicionar item ao carrinho. Produto ID: {}, Quantidade: {}",
                cartRequest.productId(), cartRequest.quantity());

        ShoppingCartResponseDTO response = shoppingCartService.addItemToCart(userId, cartRequest);

        return ResponseEntity.status(201).body(response);

    }


    @PutMapping("/items/{productId}")
    public ResponseEntity<ShoppingCartResponseDTO> updateCartItem(@PathVariable Long productId, @RequestBody UpdateCartItemQuantityDTO itemRequest) {

        Long userId = SecurityUtil.getCurrentUserId();

        log.info("Requisição PUT para atualizar item do carrinho. Usuário ID: {}, Produto ID: {}, Nova Quantidade: {}",
                userId, productId, itemRequest.quantity());

        ShoppingCartResponseDTO response = shoppingCartService.updateCartItemQuantity(
                userId,
                productId,
                itemRequest.quantity()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ShoppingCartResponseDTO> removeItemFromCart(@PathVariable Long productId) {
        Long userId = SecurityUtil.getCurrentUserId();

        log.info("Requisição DELETE para remover item do carrinho. Usuário ID: {}, Item ID: {}",
                userId, productId);

        ShoppingCartResponseDTO response = shoppingCartService.removeItemFromCart(userId, productId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items")
    public ResponseEntity<ShoppingCartResponseDTO> clearCart() {
        Long userId = SecurityUtil.getCurrentUserId();

        log.info("Requisição DELETE para limpar o carrinho. Usuário ID: {}", userId);

        ShoppingCartResponseDTO response = shoppingCartService.clearCart(userId);

        return ResponseEntity.noContent().build();
    }

}

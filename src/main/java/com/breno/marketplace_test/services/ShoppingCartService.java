package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.CartItemDTO;
import com.breno.marketplace_test.dtos.ShoppingCartRequestDTO;
import com.breno.marketplace_test.dtos.ShoppingCartResponseDTO;
import com.breno.marketplace_test.models.CartItem;
import com.breno.marketplace_test.models.Product;
import com.breno.marketplace_test.models.ShoppingCart;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.repositories.CartItemRepository;
import com.breno.marketplace_test.repositories.ProductRepository;
import com.breno.marketplace_test.repositories.ShoppingCartRepository;
import com.breno.marketplace_test.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, CartItemRepository cartItemRepository,
                              UserRepository userRepository, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<ShoppingCart> findAll() {
        return shoppingCartRepository.findAll();
    }

    public ShoppingCart findCartById(Long id) {
        return shoppingCartRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
    }

    public ShoppingCartResponseDTO saveCart(ShoppingCartRequestDTO cartDTO) {
        User user = userRepository.findById(cartDTO.userId())
                .orElseThrow(() -> new IllegalStateException("User " + cartDTO.userId() + " not found!"));

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);

        // Converter CartItemDTO para CartItem
        List<CartItem> items = cartDTO.items().stream()
                .map(itemDTO -> {
                    Product product = productRepository.findById(Math.toIntExact(itemDTO.productId()))
                            .orElseThrow(() -> new IllegalStateException("Product " + itemDTO.productId() + " not found!"));

                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setProduct(product);
                    item.setQuantity(itemDTO.quantity());
                    return item;
                })
                .collect(Collectors.toList());

        cart.setItems(items);

        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return convertToResponseDTO(savedCart);
    }

    public ShoppingCartResponseDTO updateCart(Long id, ShoppingCartRequestDTO cartDTO) {
        ShoppingCart cart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));

        User user = userRepository.findById(cartDTO.userId())
                .orElseThrow(() -> new IllegalStateException("User " + cartDTO.userId() + " not found!"));

        cart.setUser(user);

        // Atualizar itens
        cart.getItems().clear();
        List<CartItem> items = cartDTO.items().stream()
                .map(itemDTO -> {
                    Product product = productRepository.findById(Math.toIntExact(itemDTO.productId()))
                            .orElseThrow(() -> new IllegalStateException("Product " + itemDTO.productId() + " not found!"));

                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setProduct(product);
                    item.setQuantity(itemDTO.quantity());
                    return item;
                })
                .collect(Collectors.toList());

        cart.setItems(items);

        ShoppingCart updatedCart = shoppingCartRepository.save(cart);
        return convertToResponseDTO(updatedCart);
    }

    public void deleteCart(Long id) {
        ShoppingCart cart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
        shoppingCartRepository.delete(cart);
    }

    private ShoppingCartResponseDTO convertToResponseDTO(ShoppingCart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(item -> new CartItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new ShoppingCartResponseDTO(
                cart.getId(),
                cart.getUser().getId(),
                itemDTOs
        );
    }
}


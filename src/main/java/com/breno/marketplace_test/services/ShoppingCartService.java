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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("Salvando novo carrinho para o usuário: {}", cartDTO.userId());
        User user = userRepository.findById(cartDTO.userId())
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado ao salvar carrinho", cartDTO.userId());
                    return new IllegalStateException("User " + cartDTO.userId() + " not found!");
                });

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);

        // Converter CartItemDTO para CartItem
        List<CartItem> items = cartDTO.items().stream()
                .map(itemDTO -> {
                    Product product = productRepository.findById(itemDTO.productId())
                            .orElseThrow(() -> {
                                log.warn("Produto com ID {} não encontrado ao salvar carrinho", itemDTO.productId());
                                return new IllegalStateException("Product " + itemDTO.productId() + " not found!");
                            });

                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setProduct(product);
                    item.setQuantity(itemDTO.quantity());
                    return item;
                })
                .collect(Collectors.toList());

        cart.setItems(items);

        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        log.info("Carrinho salvo com sucesso. ID: {}, Usuário: {}, Itens: {}",
                savedCart.getId(), user.getEmail(), items.size());
        return convertToResponseDTO(savedCart);
    }

    public ShoppingCartResponseDTO updateCart(Long id, ShoppingCartRequestDTO cartDTO) {
        log.info("Atualizando carrinho com ID: {}", id);
        ShoppingCart cart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Carrinho com ID {} não encontrado para atualização", id);
                    return new IllegalStateException(id + " not found!");
                });

        User user = userRepository.findById(cartDTO.userId())
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado ao atualizar carrinho", cartDTO.userId());
                    return new IllegalStateException("User " + cartDTO.userId() + " not found!");
                });

        cart.setUser(user);

        // Atualizar itens
        cart.getItems().clear();
        List<CartItem> items = cartDTO.items().stream()
                .map(itemDTO -> {
                    Product product = productRepository.findById(itemDTO.productId())
                            .orElseThrow(() -> {
                                log.warn("Produto com ID {} não encontrado ao atualizar carrinho", itemDTO.productId());
                                return new IllegalStateException("Product " + itemDTO.productId() + " not found!");
                            });

                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setProduct(product);
                    item.setQuantity(itemDTO.quantity());
                    return item;
                })
                .collect(Collectors.toList());

        cart.setItems(items);

        ShoppingCart updatedCart = shoppingCartRepository.save(cart);
        log.info("Carrinho com ID {} atualizado com sucesso. Novos itens: {}", id, items.size());
        return convertToResponseDTO(updatedCart);
    }

    public void deleteCart(Long id) {
        log.info("Deletando carrinho com ID: {}", id);
        ShoppingCart cart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Carrinho com ID {} não encontrado para deleção", id);
                    return new IllegalStateException(id + " not found!");
                });
        shoppingCartRepository.delete(cart);
        log.info("Carrinho com ID {} deletado com sucesso", id);
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


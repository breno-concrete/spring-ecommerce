package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.CartItemDTO;
import com.breno.marketplace_test.dtos.ShoppingCartRequestDTO;
import com.breno.marketplace_test.dtos.ShoppingCartResponseDTO;
import com.breno.marketplace_test.exceptions.InsufficientStockException;
import com.breno.marketplace_test.mappers.ShoppingCartMapper;
import com.breno.marketplace_test.models.*;
import com.breno.marketplace_test.repositories.CartItemRepository;
import com.breno.marketplace_test.repositories.ProductRepository;
import com.breno.marketplace_test.repositories.ShoppingCartRepository;
import com.breno.marketplace_test.repositories.UserRepository;
import com.breno.marketplace_test.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional; // USE ESTA
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final ShoppingCartMapper shoppingCartMapper;


    @Transactional(readOnly = true)
    public List<ShoppingCart> findAll() {
        return shoppingCartRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ShoppingCart findCartById(Long id) {
        return shoppingCartRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
    }

    @Transactional
    public ShoppingCartResponseDTO saveCart(ShoppingCartRequestDTO cartDTO) {
        log.info("Salvando novo carrinho para o usuário: {}", cartDTO.userId());
        User user = userRepository.findById(cartDTO.userId())
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado ao salvar carrinho", cartDTO.userId());
                    return new IllegalStateException("User " + cartDTO.userId() + " not found!");
                });

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);

        validateOwnership(cart);

        // Converter CartItemDTO para CartItem
        List<CartItem> items = cartDTO.items().stream()
                .map(itemDTO -> {
                    Product product = productRepository.findById(itemDTO.productId())
                            .orElseThrow(() -> {
                                log.warn("Produto com ID {} não encontrado ao salvar carrinho", itemDTO.productId());
                                return new IllegalStateException("Product " + itemDTO.productId() + " not found!");
                            });

                    CartItem item = new CartItem();
                    item.setShoppingCart(cart);
                    item.setProduct(product);

                    if(itemDTO.quantity() > product.getStockQuantity()){
                        log.error("Quantidade solicitada para o produto com ID {} excede o estoque disponível. Solicitado: {}, Disponível: {}",
                                product.getId(), itemDTO.quantity(), product.getStockQuantity());
                        throw new InsufficientStockException("Requested quantity exceeds available stock for product " + itemDTO.productId());
                    }

                    item.setQuantity(itemDTO.quantity());
                    return item;
                })
                .collect(Collectors.toList());

        cart.setItems(items);

        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        log.info("Carrinho salvo com sucesso. ID: {}, Usuário: {}, Itens: {}",
                savedCart.getId(), user.getEmail(), items.size());

        return shoppingCartMapper.toDTO(savedCart);
    }

    @Transactional
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

        validateOwnership(cart);

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
                    item.setShoppingCart(cart);
                    item.setProduct(product);
                    item.setQuantity(itemDTO.quantity());
                    return item;
                })
                .collect(Collectors.toList());

        cart.setItems(items);

        ShoppingCart updatedCart = shoppingCartRepository.save(cart);
        log.info("Carrinho com ID {} atualizado com sucesso. Novos itens: {}", id, items.size());

        return shoppingCartMapper.toDTO(updatedCart);
    }

    @Transactional
    public void deleteCart(Long id) {
        log.info("Deletando carrinho com ID: {}", id);
        ShoppingCart cart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Carrinho com ID {} não encontrado para deleção", id);
                    return new IllegalStateException(id + " not found!");
                });
        validateOwnership(cart);

        shoppingCartRepository.delete(cart);
        log.info("Carrinho com ID {} deletado com sucesso", id);
    }

    @Transactional
    public ShoppingCartResponseDTO getCartByUserId(Long userId){
        log.info("Buscando carrinho para o usuário com ID: {}", userId);

        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Carrinho para o usuário com ID {} não encontrado", userId);
                    return new IllegalStateException("Cart for user " + userId + " not found!");
                });
        validateOwnership(cart);


        return shoppingCartMapper.toDTO(cart);
    }

    @Transactional
    public ShoppingCartResponseDTO addItemToCart(Long userId, CartItemDTO itemRequest){
        log.info("Adicionando produto ID {} ao carrinho do usuário {}", itemRequest.productId(), userId);

        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.info("Carrinho não encontrado para o usuário {}. Criando um novo...", userId);
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalStateException("Usuário não encontrado: " + userId));

                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(user);
                    return shoppingCartRepository.save(newCart);
                });

        validateOwnership(cart);

        Product product = productRepository.findById(itemRequest.productId())
                .orElseThrow(() -> new IllegalStateException("Produto não encontrado com ID: " + itemRequest.productId()));

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            incrementItemQuantity(existingItemOpt.get(), itemRequest.quantity());
        } else{

            addNewItemToCart(cart, product, itemRequest.quantity());
        }

        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDTO(savedCart);
    }

    @Transactional
    public ShoppingCartResponseDTO updateCartItemQuantity(Long userId, Long productId, Integer newQuantity){
        log.info("Atualizando quantidade do produto ID {} para {} no carrinho do usuário {}", productId, newQuantity, userId);

        if(newQuantity == null || newQuantity <= 0){
            log.warn("Tentativa de atualizar produto ID {} com quantidade inválida ({})", productId, newQuantity);
            throw new IllegalArgumentException("A quantidade do item deve ser maior que zero.");
        }


        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Carrinho não encontrado para o usuário {}", userId);
                    return new IllegalStateException("Cart for user " + userId + " not found!");
                });

        validateOwnership(cart);

        CartItem itemToUpdate = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Produto ID {} não encontrado no carrinho do usuário {}", productId, userId);
                    return new IllegalStateException("Product " + productId + " not found in the cart!");
                });

        Product product = itemToUpdate.getProduct();
        if (newQuantity > product.getStockQuantity()) {
            log.error("Estoque insuficiente para o produto {}. Solicitado: {}, Disponível: {}",
                    productId, newQuantity, product.getStockQuantity());
            throw new InsufficientStockException("Requested quantity exceeds available stock for product " + productId);
        }

        itemToUpdate.setQuantity(newQuantity);
        ShoppingCart savedCart = shoppingCartRepository.save(cart);

        log.info("Quantidade do produto ID {} atualizada com sucesso no carrinho ID {}", productId, savedCart.getId());

        return shoppingCartMapper.toDTO(savedCart);


    }

    @Transactional
    public ShoppingCartResponseDTO removeItemFromCart(Long userId, Long productId){

        log.info("Removendo produto ID {} do carrinho do usuário {}", productId, userId);

        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Cart for user " + userId + " not found!"));

        validateOwnership(cart);

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        log.info("Produto ID {} removido com sucesso do carrinho ID {}", productId, savedCart.getId());
        return shoppingCartMapper.toDTO(savedCart);
    }


    @Transactional
    public ShoppingCartResponseDTO clearCart(Long userId){
        log.info("Limpando carrinho do usuário {}", userId);

        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Cart for user " + userId + " not found!"));

        validateOwnership(cart);

        cart.getItems().clear();
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        log.info("Carrinho ID {} limpo com sucesso para o usuário {}", savedCart.getId(), userId);
        return shoppingCartMapper.toDTO(savedCart);
    }







    private void validateOwnership(ShoppingCart cart){
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Long cartId = cart.getUser().getId();

        if(!currentUserId.equals(cartId)){
            log.warn("Usuário com ID {} tentou acessar carrinho com ID {} que pertence ao usuário com ID {}", currentUserId, cart.getId(), cartId);
            throw new AccessDeniedException("You do not have permission to access this cart");
        }
    }
//------- MÉTODOS AUXILIARES ------



    private void incrementItemQuantity(CartItem item, Integer quantityToAdd) {
        item.setQuantity(item.getQuantity() + quantityToAdd);

    }

    private void addNewItemToCart(ShoppingCart cart, Product product, Integer quantity) {
        CartItem newItem = new CartItem();
        newItem.setProduct(product);
        newItem.setShoppingCart(cart);
        newItem.setQuantity(quantity);

        cart.getItems().add(newItem);
    }
}


package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.OrderItemDTO;
import com.breno.marketplace_test.dtos.OrderRequestDTO;
import com.breno.marketplace_test.dtos.OrderResponseDTO;
import com.breno.marketplace_test.mappers.OrderMapper;
import com.breno.marketplace_test.models.*;
import com.breno.marketplace_test.repositories.*;
import com.breno.marketplace_test.security.SecurityUtil;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional; // USE ESTA
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page; // USE ESTE
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable; // USE ESTE
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;



    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> findAll(Pageable pageable) {
        // 1. Busca a página de entidades do banco
        Page<Order> ordersPage = orderRepository.findAll(pageable);

        // 2. Usa o .map() da própria página para converter cada item usando o seu Mapper
        return ordersPage.map(orderMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO findOrderById(Integer id) {
        log.info("Buscando pedido com ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pedido com ID {} não encontrado", id);
                    return new IllegalStateException(id + " not found!");
                });
        validateOwnership(order);

        return convertToResponseDTO(order);

    }

    @Transactional
    public OrderResponseDTO saveOrder(OrderRequestDTO orderDTO) {
        log.info("Salvando novo pedido para o usuário: {}", orderDTO.userId());
        User user = userRepository.findById(orderDTO.userId())
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado ao salvar pedido", orderDTO.userId());
                    return new IllegalStateException("User " + orderDTO.userId() + " not found!");
                });

        Address deliveryAddress = addressRepository.findById(orderDTO.deliveryAddressId())
                .orElseThrow(() -> {
                    log.warn("Endereço com ID {} não encontrado ao salvar pedido", orderDTO.deliveryAddressId());
                    return new IllegalStateException("Address " + orderDTO.deliveryAddressId() + " not found!");
                });

        Order order = new Order();

        order.setOrderStatus(orderDTO.orderStatus());
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);


        // Converter OrderItemDTO para OrderItem
        List<OrderItem> items = orderDTO.items().stream()
                .map(itemDTO -> {

                    Product product = productRepository.findById(itemDTO.productId())
                            .orElseThrow(() -> {
                                log.warn("Produto com ID {} não encontrado ao salvar pedido", itemDTO.productId());
                                return new IllegalStateException("Product " + itemDTO.productId() + " not found!");
                            });

                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setProduct(product);

                    if(itemDTO.quantity() > product.getStockQuantity()){
                        log.warn("Quantidade solicitada ({}) para o produto com ID {} excede o estoque disponível ({})",
                                itemDTO.quantity(), itemDTO.productId(), product.getStockQuantity());
                        throw new IllegalStateException("Requested quantity exceeds available stock for product " + itemDTO.productId());
                    }

                    item.setQuantity(itemDTO.quantity());

                    item.setPricePurchased(itemDTO.pricePurchased());

                    product.decreaseStock(itemDTO.quantity());
                    return item;
                })
                .collect(Collectors.toList());


        order.setItems(items);



        Order savedOrder = orderRepository.save(order);
        log.info("Pedido salvo com sucesso. ID: {}, Usuário: {}, Status: {}, Itens: {}",
                savedOrder.getId(), user.getEmail(), savedOrder.getOrderStatus(), items.size());
        return convertToResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO updateOrder(Integer id, OrderRequestDTO orderDTO) {
        log.info("Atualizando pedido com ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pedido com ID {} não encontrado para atualização", id);
                    return new IllegalStateException(id + " not found!");
                });

        User user = userRepository.findById(orderDTO.userId())
                .orElseThrow(() -> {
                    log.warn("Usuário com ID {} não encontrado ao atualizar pedido", orderDTO.userId());
                    return new IllegalStateException("User " + orderDTO.userId() + " not found!");
                });

        Address deliveryAddress = addressRepository.findById(orderDTO.deliveryAddressId())
                .orElseThrow(() -> {
                    log.warn("Endereço com ID {} não encontrado ao atualizar pedido", orderDTO.deliveryAddressId());
                    return new IllegalStateException("Address " + orderDTO.deliveryAddressId() + " not found!");
                });

        validateOwnership(order);


        order.setOrderStatus(orderDTO.orderStatus());
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);




        // Atualizar itens
        order.getItems().clear();
        List<OrderItem> items = orderDTO.items().stream()
                .map(itemDTO -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);

                    Product product = productRepository.findById(itemDTO.productId())
                            .orElseThrow(() -> {
                                log.warn("Produto com ID {} não encontrado ao atualizar pedido", itemDTO.productId());
                                return new IllegalStateException("Product " + itemDTO.productId() + " not found!");
                            });

                    if(itemDTO.quantity() > product.getStockQuantity()){
                        log.warn("Quantidade solicitada ({}) para o produto com ID {} excede o estoque disponível ({})",
                                itemDTO.quantity(), itemDTO.productId(), product.getStockQuantity());
                        throw new IllegalStateException("Requested quantity exceeds available stock for product " + itemDTO.productId());
                    }

                    item.setQuantity(itemDTO.quantity());
                    item.setPricePurchased(itemDTO.pricePurchased());
                    return item;
                })
                .collect(Collectors.toList());

        order.setItems(items);

        Order savedOrder = orderRepository.save(order);

        shoppingCartRepository.findByUserId(orderDTO.userId())
                .ifPresent(cart -> {
                    shoppingCartRepository.delete(cart);
                    log.info("Carrinho do usuário {} limpo após criação do pedido {}",
                            orderDTO.userId(), savedOrder.getId()); // savedOrder existe aqui
                });

        return convertToResponseDTO(savedOrder);
    }

    @Transactional
    public void deleteOrder(Integer id) {
        log.info("Deletando pedido com ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pedido com ID {} não encontrado para deleção", id);
                    return new IllegalStateException(id + " not found!");
                });

        validateOwnership(order);

        orderRepository.delete(order);
        log.info("Pedido com ID {} deletado com sucesso", id);
    }

    private void validateOwnership(Order order){
        Long currentUserId = SecurityUtil.getCurrentUserId();
        Long orderId = order.getUser().getId();

        if(!currentUserId.equals(orderId)){
            log.warn("Usuário com ID {} tentou acessar pedido com ID {} que pertence ao usuário com ID {}", currentUserId, order.getId(), orderId);
            throw new AccessDeniedException("You do not have permission to access this order");
        }
    }

    private OrderResponseDTO convertToResponseDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getPricePurchased()
                ))
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                order.getCreatedAt(),
                order.getOrderStatus(),
                order.getUser().getId(),
                order.getDeliveryAddress().getId(),
                itemDTOs
        );
    }
}


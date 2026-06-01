package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.OrderItemDTO;
import com.breno.marketplace_test.dtos.OrderRequestDTO;
import com.breno.marketplace_test.dtos.OrderResponseDTO;
import com.breno.marketplace_test.models.*;
import com.breno.marketplace_test.repositories.AddressRepository;
import com.breno.marketplace_test.repositories.OrderRepository;
import com.breno.marketplace_test.repositories.ProductRepository;
import com.breno.marketplace_test.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page; // USE ESTE
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable; // USE ESTE
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, AddressRepository addressRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
    }

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order findOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
    }

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
                    item.setQuantity(itemDTO.quantity());
                    item.setPricePurchased(itemDTO.pricePurchased());
                    return item;
                })
                .collect(Collectors.toList());

        order.setItems(items);

        Order savedOrder = orderRepository.save(order);
        log.info("Pedido salvo com sucesso. ID: {}, Usuário: {}, Status: {}, Itens: {}",
                savedOrder.getId(), user.getEmail(), savedOrder.getOrderStatus(), items.size());
        return convertToResponseDTO(savedOrder);
    }

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

        order.setOrderStatus(orderDTO.orderStatus());
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);

        // Atualizar itens
        order.getItems().clear();
        List<OrderItem> items = orderDTO.items().stream()
                .map(itemDTO -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setQuantity(itemDTO.quantity());
                    item.setPricePurchased(itemDTO.pricePurchased());
                    return item;
                })
                .collect(Collectors.toList());

        order.setItems(items);

        Order updatedOrder = orderRepository.save(order);
        log.info("Pedido com ID {} atualizado com sucesso. Novo status: {}", id, updatedOrder.getOrderStatus());
        return convertToResponseDTO(updatedOrder);
    }

    public void deleteOrder(Integer id) {
        log.info("Deletando pedido com ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pedido com ID {} não encontrado para deleção", id);
                    return new IllegalStateException(id + " not found!");
                });
        orderRepository.delete(order);
        log.info("Pedido com ID {} deletado com sucesso", id);
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


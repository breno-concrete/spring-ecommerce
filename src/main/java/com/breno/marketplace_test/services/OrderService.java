package com.breno.marketplace_test.services;

import com.breno.marketplace_test.dtos.OrderItemDTO;
import com.breno.marketplace_test.dtos.OrderRequestDTO;
import com.breno.marketplace_test.dtos.OrderResponseDTO;
import com.breno.marketplace_test.models.Address;
import com.breno.marketplace_test.models.Order;
import com.breno.marketplace_test.models.OrderItem;
import com.breno.marketplace_test.models.User;
import com.breno.marketplace_test.repositories.AddressRepository;
import com.breno.marketplace_test.repositories.OrderRepository;
import com.breno.marketplace_test.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
    }

    public OrderResponseDTO saveOrder(OrderRequestDTO orderDTO) {
        User user = userRepository.findById(orderDTO.userId())
                .orElseThrow(() -> new IllegalStateException("User " + orderDTO.userId() + " not found!"));

        Address deliveryAddress = addressRepository.findById(orderDTO.deliveryAddressId())
                .orElseThrow(() -> new IllegalStateException("Address " + orderDTO.deliveryAddressId() + " not found!"));

        Order order = new Order();
        order.setCreationTime(LocalDateTime.now());
        order.setOrderStatus(orderDTO.orderStatus());
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);

        // Converter OrderItemDTO para OrderItem
        List<OrderItem> items = orderDTO.items().stream()
                .map(itemDTO -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setQuanitty(itemDTO.quantity());
                    item.setPricePurchased(itemDTO.pricePurchased());
                    return item;
                })
                .collect(Collectors.toList());

        order.setItems(items);

        Order savedOrder = orderRepository.save(order);
        return convertToResponseDTO(savedOrder);
    }

    public OrderResponseDTO updateOrder(Integer id, OrderRequestDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));

        User user = userRepository.findById(orderDTO.userId())
                .orElseThrow(() -> new IllegalStateException("User " + orderDTO.userId() + " not found!"));

        Address deliveryAddress = addressRepository.findById(orderDTO.deliveryAddressId())
                .orElseThrow(() -> new IllegalStateException("Address " + orderDTO.deliveryAddressId() + " not found!"));

        order.setOrderStatus(orderDTO.orderStatus());
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);

        // Atualizar itens
        order.getItems().clear();
        List<OrderItem> items = orderDTO.items().stream()
                .map(itemDTO -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setQuanitty(itemDTO.quantity());
                    item.setPricePurchased(itemDTO.pricePurchased());
                    return item;
                })
                .collect(Collectors.toList());

        order.setItems(items);

        Order updatedOrder = orderRepository.save(order);
        return convertToResponseDTO(updatedOrder);
    }

    public void deleteOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found!"));
        orderRepository.delete(order);
    }

    private OrderResponseDTO convertToResponseDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuanitty(),
                        item.getPricePurchased()
                ))
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                order.getCreationTime(),
                order.getOrderStatus(),
                order.getUser().getId(),
                order.getDeliveryAddress().getId(),
                itemDTOs
        );
    }
}


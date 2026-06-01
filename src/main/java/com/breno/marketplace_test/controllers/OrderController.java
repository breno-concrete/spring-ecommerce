package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.OrderRequestDTO;
import com.breno.marketplace_test.dtos.OrderResponseDTO;
import com.breno.marketplace_test.models.Order;
import com.breno.marketplace_test.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@Slf4j
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "List all orders", description = "Returns a list of all orders")
    public ResponseEntity<Page<OrderResponseDTO>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creationTime"));

        Page<OrderResponseDTO> pedidosPaginados = orderService.findAll(pageable);
        return ResponseEntity.ok(pedidosPaginados);
    }

    @PostMapping
    @Operation(summary = "Create Order", description = "Creates a new order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO order) {
        log.info("Requisição POST para criar novo pedido. Usuário: {}", order.userId());
        OrderResponseDTO response = orderService.saveOrder(order);
        log.info("Pedido criado com sucesso. ID: {}, Status: {}", response.id(), response.orderStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Order", description = "Returns an order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderResponseDTO getOrderById(@PathVariable Integer id) {
        return orderService.findOrderById(id);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update Order", description = "Updates an order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order updated"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Integer id, @Valid @RequestBody OrderRequestDTO order) {
        log.info("Requisição PUT para atualizar pedido com ID: {}", id);
        OrderResponseDTO response = orderService.updateOrder(id, order);
        log.info("Pedido com ID {} atualizado com sucesso. Novo status: {}", id, response.orderStatus());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete Order", description = "Deletes an order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        log.info("Requisição DELETE para deletar pedido com ID: {}", id);
        orderService.deleteOrder(id);
        log.info("Pedido com ID {} deletado com sucesso", id);
        return ResponseEntity.ok("Order deleted successfully!");
    }
}


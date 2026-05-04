package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.OrderRequestDTO;
import com.breno.marketplace_test.dtos.OrderResponseDTO;
import com.breno.marketplace_test.models.Order;
import com.breno.marketplace_test.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "List all orders", description = "Returns a list of all orders")
    public List<Order> getOrders() {
        return orderService.findAll();
    }

    @PostMapping
    @Operation(summary = "Create Order", description = "Creates a new order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO order) {
        return ResponseEntity.ok(orderService.saveOrder(order));
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Order", description = "Returns an order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public Order getOrderById(@PathVariable Integer id) {
        return orderService.findOrderById(id);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update Order", description = "Updates an order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order updated"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Integer id, @RequestBody OrderRequestDTO order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete Order", description = "Deletes an order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully!");
    }
}


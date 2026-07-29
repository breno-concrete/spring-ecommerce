package com.breno.marketplace_test.controllers;

import com.breno.marketplace_test.dtos.OrderRequestDTO;
import com.breno.marketplace_test.dtos.OrderResponseDTO;
import com.breno.marketplace_test.dtos.OrderStatusUpdateDTO;
import com.breno.marketplace_test.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Criação, consulta e atualização de pedidos de compra")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(
            summary = "Listar pedidos do usuário",
            description = "Retorna uma lista paginada dos pedidos pertencentes ao usuário autenticado. Permite ordenação e controle de paginação pelos parâmetros da URL."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)")
    })
    public ResponseEntity<Page<OrderResponseDTO>> getOrders(
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        log.info("Requisição GET para listar pedidos do usuário logado");

        Page<OrderResponseDTO> pageOrder = orderService.findAll(pageable);

        return ResponseEntity.ok(pageOrder);
    }

    @PostMapping
    @Operation(
            summary = "Criar novo pedido",
            description = "Gera um novo pedido de compra. Valida o estoque, abate as quantidades dos produtos solicitados e associa a compra ao usuário logado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou quantidade indisponível em estoque"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "404", description = "Produto ou endereço de entrega não encontrado")
    })
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO order) {
        log.info("Requisição POST para criar novo pedido. Usuário: {}", order.userId());
        OrderResponseDTO response = orderService.saveOrder(order);
        log.info("Pedido criado com sucesso. ID: {}, Status: {}", response.id(), response.orderStatus());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Buscar pedido por ID",
            description = "Retorna os detalhes completos de um pedido específico, garantindo que o usuário logado só possa acessar os seus próprios pedidos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Tentativa de acessar o pedido de outro usuário)"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public OrderResponseDTO getOrderById(@PathVariable Integer id) {
        return orderService.findOrderById(id);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Atualizar pedido",
            description = "Atualiza integralmente as informações e a lista de itens de um pedido existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Integer id, @Valid @RequestBody OrderRequestDTO order) {
        log.info("Requisição PUT para atualizar pedido com ID: {}", id);
        OrderResponseDTO response = orderService.updateOrder(id, order);
        log.info("Pedido com ID {} atualizado com sucesso. Novo status: {}", id, response.orderStatus());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Deletar pedido",
            description = "Remove permanentemente um pedido do sistema com base no ID fornecido."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso (No Content)"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        log.info("Requisição DELETE para deletar pedido com ID: {}", id);
        orderService.deleteOrder(id);
        log.info("Pedido com ID {} deletado com sucesso", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/status")
    @Operation(
            summary = "Atualizar status do pedido",
            description = "Atualiza especificamente o status de um pedido (ex: de PENDENTE para CANCELADO ou ENTREGUE), sem alterar os itens comprados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Transição de status inválida (ex: tentar alterar de CANCELADO para ENTREGUE)"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Integer id, @Valid @RequestBody OrderStatusUpdateDTO requestDTO) {
        log.info("Requisição PATCH para atualizar status do pedido com ID: {}. Novo status: {}", id, requestDTO.status());


        OrderResponseDTO response = orderService.updateOrderStatus(id, requestDTO.status());
        log.info("Status do pedido com ID {} atualizado com sucesso para {}", id, response.orderStatus());

        return ResponseEntity.ok(response);

    }
}


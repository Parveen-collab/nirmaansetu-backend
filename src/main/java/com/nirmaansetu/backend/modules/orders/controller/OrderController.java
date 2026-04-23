package com.nirmaansetu.backend.modules.orders.controller;

import com.nirmaansetu.backend.modules.orders.dto.OrderResponseDto;
import com.nirmaansetu.backend.modules.orders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order APIs", description = "Operations related to orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Get all orders",
            description = "Returns a list of all orders in the system.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(
            summary = "Get order by ID",
            description = "Returns details of a specific order by its unique identifier.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(
            summary = "Get user orders",
            description = "Returns a list of all orders for a specific user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }
}

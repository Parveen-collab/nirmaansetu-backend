package com.nirmaansetu.backend.modules.orders.dto;

import com.nirmaansetu.backend.statemachine.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private Long userId;
    private String userName;
    private OrderState status;
    private Double totalAmount;
    private List<OrderItemResponseDto> items;
    private Instant createdAt;
    private Instant updatedAt;
}

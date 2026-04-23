package com.nirmaansetu.backend.modules.orders.service;

import com.nirmaansetu.backend.modules.orders.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto> getAllOrders();
    OrderResponseDto getOrderById(Long id);
    List<OrderResponseDto> getOrdersByUserId(Long userId);
}

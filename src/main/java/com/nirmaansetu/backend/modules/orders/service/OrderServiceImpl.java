package com.nirmaansetu.backend.modules.orders.service;

import com.nirmaansetu.backend.modules.orders.dto.OrderResponseDto;
import com.nirmaansetu.backend.modules.orders.mapper.OrderMapper;
import com.nirmaansetu.backend.modules.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        return orderMapper.toResponseDtoList(orderRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        return orderMapper.toResponseDtoList(orderRepository.findByUserId(userId));
    }
}

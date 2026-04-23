package com.nirmaansetu.backend.modules.orders.repository;

import com.nirmaansetu.backend.modules.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    java.util.List<Order> findByUserId(Long userId);
}

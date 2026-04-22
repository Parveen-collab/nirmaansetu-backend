package com.nirmaansetu.backend.modules.payment.repository;

import com.nirmaansetu.backend.modules.payment.entity.Payment;
import com.nirmaansetu.backend.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(User user);
    List<Payment> findByUserId(Long userId);
}

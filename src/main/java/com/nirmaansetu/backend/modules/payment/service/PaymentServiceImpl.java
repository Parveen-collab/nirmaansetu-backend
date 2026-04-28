package com.nirmaansetu.backend.modules.payment.service;

import com.nirmaansetu.backend.modules.notifications.service.NotificationService;
import com.nirmaansetu.backend.modules.payment.dto.PaymentDto;
import com.nirmaansetu.backend.modules.payment.dto.PaymentSummaryDto;
import com.nirmaansetu.backend.modules.payment.entity.Payment;
import com.nirmaansetu.backend.modules.payment.entity.PaymentStatus;
import com.nirmaansetu.backend.modules.payment.mapper.PaymentMapper;
import com.nirmaansetu.backend.modules.payment.repository.PaymentRepository;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationService notificationService;

    @Override
    public PaymentDto processPayment(PaymentDto paymentDto) {
        User user = userRepository.findById(paymentDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Payment payment = paymentMapper.toEntity(paymentDto);
        payment.setUser(user);
        
        // Demo logic: If transactionId is null, generate one and set status to SUCCESS
        if (payment.getTransactionId() == null) {
            payment.setTransactionId("TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.SUCCESS);
        }

        Payment savedPayment = paymentRepository.save(payment);

        if (savedPayment.getStatus() == PaymentStatus.SUCCESS) {
            notificationService.createNotification(
                    user,
                    "Payment Successful",
                    String.format("Your payment of ₹%s for %s was successful. Transaction ID: %s",
                            savedPayment.getAmount(), savedPayment.getDescription(), savedPayment.getTransactionId())
            );
        }

        return paymentMapper.toDto(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getAllTransactions() {
        return paymentMapper.toDtoList(paymentRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getUserTransactions(Long userId) {
        return paymentMapper.toDtoList(paymentRepository.findByUserId(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentSummaryDto getPaymentSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Payment> userPayments = paymentRepository.findByUser(user);
        
        BigDecimal totalAmount = userPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return PaymentSummaryDto.builder()
                .userId(userId)
                .totalAmount(totalAmount)
                .registrationDate(user.getCreatedAt())
                .build();
    }
}

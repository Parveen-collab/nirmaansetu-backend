package com.nirmaansetu.backend.modules.payment.dto;

import com.nirmaansetu.backend.modules.payment.entity.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PaymentDto {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String transactionId;
    private PaymentStatus status;
    private String description;
    private Instant createdAt;
}

package com.nirmaansetu.backend.modules.payment.service;

import com.nirmaansetu.backend.modules.payment.dto.PaymentDto;
import com.nirmaansetu.backend.modules.payment.dto.PaymentSummaryDto;

import java.util.List;

public interface PaymentService {
    PaymentDto processPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllTransactions();
    List<PaymentDto> getUserTransactions(Long userId);
    PaymentSummaryDto getPaymentSummary(Long userId);
}

package com.nirmaansetu.backend.modules.payment.controller;

import com.nirmaansetu.backend.modules.payment.dto.PaymentDto;
import com.nirmaansetu.backend.modules.payment.dto.PaymentSummaryDto;
import com.nirmaansetu.backend.modules.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment APIs", description = "Operations related to payments and transactions")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            summary = "Process a new payment",
            description = "Creates a new payment record for a user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<PaymentDto> processPayment(@RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(paymentService.processPayment(paymentDto));
    }

    @Operation(
            summary = "Get all transactions",
            description = "Returns a list of all transactions in the system.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllTransactions() {
        return ResponseEntity.ok(paymentService.getAllTransactions());
    }

    @Operation(
            summary = "Get user transactions",
            description = "Returns a list of all transactions for a specific user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDto>> getUserTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getUserTransactions(userId));
    }

    @Operation(
            summary = "Get payment summary",
            description = "Returns a summary of payments for a specific user, including total amount and registration date.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/summary/{userId}")
    public ResponseEntity<PaymentSummaryDto> getPaymentSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getPaymentSummary(userId));
    }
}

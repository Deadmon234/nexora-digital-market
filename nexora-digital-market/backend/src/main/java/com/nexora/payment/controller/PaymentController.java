package com.nexora.payment.controller;

import com.nexora.payment.dto.PaymentDto;
import com.nexora.payment.dto.PaymentRequest;
import com.nexora.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Paiements")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Payer une commande")
    public ResponseEntity<PaymentDto> pay(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.processPayment(request));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Paiement d'une commande")
    public PaymentDto getByOrder(@PathVariable Long orderId) {
        return paymentService.getPaymentForOrder(orderId);
    }
}

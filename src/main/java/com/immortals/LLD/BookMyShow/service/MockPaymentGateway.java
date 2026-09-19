package com.immortals.LLD.BookMyShow.service;

import com.immortals.LLD.BookMyShow.entity.Payment;
import com.immortals.LLD.BookMyShow.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class MockPaymentGateway implements PaymentGateway {

    @Override
    public Payment pay(String bookingId, BigDecimal amount, String idempotencyKey) {
        Payment payment = new Payment(UUID.randomUUID().toString(), bookingId, amount);
        payment.setStatus(PaymentStatus.SUCCESS);
        return payment;
    }
}
package com.immortals.LLD.BookMyShow.service;

import com.immortals.LLD.BookMyShow.entity.Payment;

import java.math.BigDecimal;

public interface PaymentGateway {

    Payment pay(
            String bookingId,
            BigDecimal amount,
            String idempotencyKey);
}


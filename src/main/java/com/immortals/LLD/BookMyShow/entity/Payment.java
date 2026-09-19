package com.immortals.LLD.BookMyShow.entity;


import com.immortals.LLD.BookMyShow.enums.PaymentStatus;

import java.math.BigDecimal;

public class Payment {

    private final String id;
    private final String bookingId;
    private final BigDecimal amount;
    private PaymentStatus status;

    public Payment(String id, String bookingId, BigDecimal amount) {

        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
        this.status = PaymentStatus.INITIATED;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
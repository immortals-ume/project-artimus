package com.immortals.LLD.BookMyShow.entity;


import com.immortals.LLD.BookMyShow.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class Booking {
    private final String id;
    private final long userId;
    private final long showId;
    private final List<Long> seatIds;
    private final BigDecimal amount;
    private final Instant createdAt;
    private BookingStatus status;
    private Instant expiresAt;

    public Booking(
            String id,
            long userId,
            long showId,
            List<Long> seatIds,
            BigDecimal amount) {

        this.id = id;
        this.userId = userId;
        this.showId = showId;
        this.seatIds = List.copyOf(seatIds);
        this.amount = amount;
        this.status = BookingStatus.PENDING_PAYMENT;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getShowId() {
        return showId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", showId=" + showId +
                ", seats=" + seatIds +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}

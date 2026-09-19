package com.immortals.LLD.BookMyShow.entity;



import com.immortals.LLD.BookMyShow.enums.SeatStatus;

import java.time.Instant;

public class ShowSeat {
    private final long showId;
    private final long seatId;
    private SeatStatus status;
    private String bookingId;
    private Instant lockExpiry;

    public ShowSeat(long showId, long seatId) {
        this.showId = showId;
        this.seatId = seatId;
        this.status = SeatStatus.AVAILABLE;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Instant getLockExpiry() {
        return lockExpiry;
    }

    public void setLockExpiry(Instant lockExpiry) {
        this.lockExpiry = lockExpiry;
    }

    public long getSeatId() {
        return seatId;
    }
}

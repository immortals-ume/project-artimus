package com.immortals.LLD.BookMyShow.repositories;

import com.immortals.LLD.BookMyShow.entity.ShowSeat;

import java.util.List;

public interface ShowSeatRepository {

    ShowSeat find(long showId, long seatId);

    List<ShowSeat> findAll(long showId, List<Long> seatIds);

    void save(ShowSeat showSeat);
}



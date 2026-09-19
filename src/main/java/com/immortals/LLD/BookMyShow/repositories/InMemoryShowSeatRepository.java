package com.immortals.LLD.BookMyShow.repositories;

import com.immortals.LLD.BookMyShow.entity.ShowSeat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryShowSeatRepository implements ShowSeatRepository {

    private final Map<String, ShowSeat> seats = new ConcurrentHashMap<>();

    private String key(long showId, long seatId) {
        return showId + ":" + seatId;
    }

    @Override
    public ShowSeat find(long showId, long seatId) {
        return seats.get(key(showId, seatId));
    }

    @Override
    public List<ShowSeat> findAll(long showId, List<Long> seatIds) {

        return seatIds.stream().map(id -> find(showId, id)).toList();
    }

    @Override
    public void save(ShowSeat showSeat) {
        seats.put(key(getShowId(showSeat), showSeat.getSeatId()), showSeat);
    }

    private long getShowId(ShowSeat seat) {
        try {
            var field = ShowSeat.class.getDeclaredField("showId");

            field.setAccessible(true);

            return field.getLong(seat);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
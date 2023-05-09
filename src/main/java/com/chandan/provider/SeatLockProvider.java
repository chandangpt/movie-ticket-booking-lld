package com.chandan.provider;

import com.chandan.model.Seat;
import com.chandan.model.Show;

import java.util.List;

public interface SeatLockProvider {
    void lockSeats(String user, List<Seat> seats, Show show);
    void unlockSeats(String user, List<Seat> seats, Show show);
    boolean validateLock(String user, Seat seat, Show show);
    List<Seat> getLockedSeats(Show show);

}

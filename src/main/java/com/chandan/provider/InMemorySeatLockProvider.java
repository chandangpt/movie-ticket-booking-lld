package com.chandan.provider;

import com.chandan.exceptions.SeatTemporaryUnavailableException;
import com.chandan.model.Seat;
import com.chandan.model.SeatLock;
import com.chandan.model.Show;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;

import java.util.*;

public class InMemorySeatLockProvider implements SeatLockProvider {

    private final Integer lockTimeout;
    private final Map<Show, Map<Seat, SeatLock>> locks;

    public InMemorySeatLockProvider(@NonNull final Integer lockTimeout) {
        this.locks = new HashMap<>();
        this.lockTimeout = lockTimeout;
    }
    @Override
    synchronized public void lockSeats(String user, List<Seat> seats, Show show) {
        for(Seat seat : seats) {
            if(isSeatLocked(show, seat)) {
                throw new SeatTemporaryUnavailableException();
            }
        }
        for(Seat seat : seats) {
            lockSeat(user, show, seat);
        }
    }

    private void lockSeat(final String user, final Show show, final Seat seat) {
        if(!locks.containsKey(show)) {
            locks.put(show, new HashMap<>());
        }
        final SeatLock lock = new SeatLock(seat, show, lockTimeout, new Date(), user);
        locks.get(show).put(seat, lock);
    }

    private boolean isSeatLocked(final Show show, final Seat seat) {
        return locks.containsKey(show) && locks.get(show).containsKey(seat) && !locks.get(show).get(seat).isLockExpired();
    }

    @Override
    public void unlockSeats(String user, List<Seat> seats, Show show) {
        for(Seat seat : seats) {
            if(validateLock(user, seat, show)) {
                unlockSeat(seat, show);
            }
        }
    }

    public void unlockSeat(Seat seat, Show show) {
        if(!locks.containsKey(show)) {
            return;
        }
        locks.get(show).remove(seat);
    }

    @Override
    public boolean validateLock(String user, Seat seat, Show show) {
        return isSeatLocked(show, seat) && locks.get(show).get(seat).getLockedBy().equals(user);
    }

    @Override
    public List<Seat> getLockedSeats(Show show) {
        if(!locks.containsKey(show)) {
            return ImmutableList.of();
        }
        final List<Seat> lockedSeats = new ArrayList<>();
        for(Seat seat : locks.get(show).keySet()) {
            if(isSeatLocked(show, seat)) {
                lockedSeats.add(seat);
            }
        }
        return lockedSeats;
    }
}

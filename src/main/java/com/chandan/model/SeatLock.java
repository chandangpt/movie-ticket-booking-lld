package com.chandan.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@Getter
public class SeatLock {
    private Seat seat;
    private Show show;
    private Integer timeoutInSeconds;
    private Date lockTime;
    private String lockedBy;

    public boolean isLockExpired() {
        final Instant lockInstance = lockTime.toInstant().plusSeconds(timeoutInSeconds);
        final Instant currentInstance = new Date().toInstant();
        return lockInstance.isBefore(currentInstance);
    }
}

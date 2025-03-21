package com.chandan.services;

import com.chandan.exceptions.BadRequestException;
import com.chandan.model.Booking;
import com.chandan.model.SeatLock;
import com.chandan.provider.SeatLockProvider;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class PaymentsService {
    Map<Booking, Integer> bookingFailures;
    private final Integer allowedRetries;
    private final SeatLockProvider seatLockProvider;

    public PaymentsService(@NonNull final Integer allowedRetries, SeatLockProvider seatLockProvider) {
        this.allowedRetries = allowedRetries;
        this.seatLockProvider = seatLockProvider;
        bookingFailures = new HashMap<>();
    }

    public void processPaymentFailed(@NonNull final Booking booking, @NonNull final String user) {
        if(!booking.getUser().equals(user)) {
            throw new BadRequestException();
        }
        if(!bookingFailures.containsKey(booking)) {
            bookingFailures.put(booking, 0);
        }
        final Integer currentFailureCount = bookingFailures.get(booking);
        final Integer newFailureCount = currentFailureCount + 1;
        bookingFailures.put(booking, newFailureCount);
        if(newFailureCount > allowedRetries) {
            seatLockProvider.unlockSeats(booking.getUser(), booking.getSeatsBooked(), booking.getShow());
        }
    }
}

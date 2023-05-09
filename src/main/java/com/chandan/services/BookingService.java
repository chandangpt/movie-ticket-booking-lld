package com.chandan.services;

import com.chandan.exceptions.BadRequestException;
import com.chandan.exceptions.NotFoundException;
import com.chandan.exceptions.SeatPermanentlyUnavailableException;
import com.chandan.model.Booking;
import com.chandan.model.Seat;
import com.chandan.model.Show;
import com.chandan.provider.SeatLockProvider;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class BookingService {

    private final Map<String, Booking> bookings;
    private final SeatLockProvider seatLockProvider;

    public BookingService(SeatLockProvider seatLockProvider) {
        this.seatLockProvider = seatLockProvider;
        bookings = new HashMap<>();
    }

    public Booking getBooking(String bookingId) {
        if(!bookings.containsKey(bookingId)) {
            throw new NotFoundException();
        }
        return bookings.get(bookingId);
    }

    public List<Booking> getBookings(@NonNull final Show show) {
        List<Booking> allBookings = new ArrayList<>();
        for(Booking booking: bookings.values()) {
            if(booking.getShow().equals(show)) {
                allBookings.add(booking);
            }
        }
        return allBookings;
    }

    public Booking createBooking(@NonNull final String userId, @NonNull final Show show, @NonNull final List<Seat> seats) {
        if(isAnySeatAlreadyBooked(show, seats)) {
            throw new SeatPermanentlyUnavailableException();
        }
        seatLockProvider.lockSeats(userId, seats, show);
        final String bookingId = UUID.randomUUID().toString();
        final Booking newBooking = new Booking(bookingId, seats, show, userId);
        bookings.put(bookingId, newBooking);
        return newBooking;
    }

    private boolean isAnySeatAlreadyBooked(final Show show, final List<Seat> seats) {
        final List<Seat> bookedSeats = getBookedSeats(show);
        for(Seat seat : seats) {
            if(bookedSeats.contains(seat))
                return true;
        }
        return false;
    }

    public List<Seat> getBookedSeats(@NonNull final Show show) {
        return getBookings(show).stream().filter(Booking::isConfirmed).map(Booking::getSeatsBooked).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public void confirmBooking(@NonNull final Booking booking, @NonNull final String user) {
        if(!booking.getUser().equals(user)) {
            throw new BadRequestException();
        }
        for(Seat seat : booking.getSeatsBooked()) {
            if(!seatLockProvider.validateLock(user, seat, booking.getShow())) {
                throw new BadRequestException();
            }
        }
        booking.confirmBooking();
    }
}

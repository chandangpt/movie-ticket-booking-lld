package com.chandan.model;

import com.chandan.exceptions.InvalidStateException;
import lombok.Getter;

import java.util.List;
@Getter
public class Booking {
    private final String id;
    private final List<Seat> seatsBooked;
    private final Show show;
    private final String user;
    private BookingStatus bookingStatus;
    public Booking(String id, List<Seat> seatsBooked, Show show, String user) {
        this.id = id;
        this.seatsBooked = seatsBooked;
        this.show = show;
        this.user = user;
        bookingStatus = BookingStatus.CREATED;
    }

    public boolean isConfirmed() {
        return this.bookingStatus == BookingStatus.CONFIRMED;
    }

    public void confirmBooking() {
        if(this.bookingStatus == BookingStatus.CREATED) {
            this.bookingStatus = BookingStatus.CONFIRMED;
        } else {
            throw new InvalidStateException();
        }
    }

    public void expireBooking() {
        if(this.bookingStatus != BookingStatus.CREATED) {
            throw new InvalidStateException();
        }
        this.bookingStatus = BookingStatus.EXPIRED;
    }
}

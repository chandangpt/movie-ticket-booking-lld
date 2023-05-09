package com.chandan.api;

import com.chandan.model.Seat;
import com.chandan.model.Show;
import com.chandan.model.Theatre;
import com.chandan.services.BookingService;
import com.chandan.services.ShowService;
import com.chandan.services.TheatreService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
public class BookingController {
    private final ShowService showService;
    private final BookingService bookingService;
    private final TheatreService theatreService;

    public String createBooking(@NonNull final String userId, @NonNull final String showId, @NonNull final List<String> seatIds) {
        final Show show = showService.getShow(showId);
        final List<Seat> seats = seatIds.stream().map(theatreService::getSeat).collect(Collectors.toList());
        return bookingService.createBooking(userId, show, seats).getId();

    }
}

package com.chandan.api;

import com.chandan.model.*;
import com.chandan.services.MovieService;
import com.chandan.services.SeatAvailabilityService;
import com.chandan.services.ShowService;
import com.chandan.services.TheatreService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShowController {
    private final SeatAvailabilityService seatAvailabilityService;
    private final ShowService showService;
    private final TheatreService theatreService;
    private final MovieService movieService;

    public String createShow(String movieId, String screenId, Date startTime, Integer durationInSeconds) {
        final Movie movie = movieService.getMovie(movieId);
        final Screen screen = theatreService.getScreen(screenId);
        return showService.createShow(movie, screen, startTime, durationInSeconds).getId();
    }

    public List<String> getAvailableSeats(@NonNull final String showId) {
        final Show show = showService.getShow(showId);
        final List<Seat> availableSeats = seatAvailabilityService.getAvailableSeats(show);
        return availableSeats.stream().map(Seat::getId).collect(Collectors.toList());
    }
}

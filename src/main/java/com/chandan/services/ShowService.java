package com.chandan.services;

import com.chandan.exceptions.NotFoundException;
import com.chandan.exceptions.ScreenAlreadyOccupiedException;
import com.chandan.model.Movie;
import com.chandan.model.Screen;
import com.chandan.model.Show;
import lombok.NonNull;

import java.util.*;

public class ShowService {
    private final Map<String, Show> shows;

    public ShowService() {
        shows = new HashMap<>();
    }

    public Show getShow(@NonNull final String showId) {
        if(!shows.containsKey(showId)) {
            throw new NotFoundException();
        }
        return shows.get(showId);
    }

    public Show createShow(@NonNull final Movie movie, @NonNull final Screen screen, @NonNull final Date startTime, @NonNull final Integer durationInSeconds) {
        if(!checkIfShowCreationAllowed(screen, startTime, durationInSeconds)) {
            throw  new ScreenAlreadyOccupiedException();
        }
        String showId = UUID.randomUUID().toString();
        final Show show = new Show(showId, movie, screen, startTime, durationInSeconds);
        this.shows.put(showId, show);
        return show;
    }

    private List<Show> getShowsForScreen(final Screen screen) {
        final List<Show> showsList = new ArrayList<>();
        for(Show show : shows.values()) {
            if(show.getScreen().equals(screen))
                showsList.add(show);
        }
        return showsList;
    }

    private boolean checkIfShowCreationAllowed(final Screen screen, final Date startTime, final Integer durationInSeconds) {
        return true;
    }
}

package com.chandan.services;

import com.chandan.exceptions.BadRequestException;
import com.chandan.exceptions.NotFoundException;
import com.chandan.model.Movie;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MovieService {
    private final Map<String, Movie> movies;

    public MovieService() {
        movies = new HashMap<>();
    }

    public Movie createMovie(@NonNull final String movieName) {
        String movieId = UUID.randomUUID().toString();
        Movie movie = new Movie(movieId, movieName);
        movies.put(movieId, movie);
        return movie;
    }

    public Movie getMovie(@NonNull final String movieId) {
        if(!movies.containsKey(movieId)) {
            throw new NotFoundException();
        }
        return movies.get(movieId);
    }

}

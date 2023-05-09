package com.chandan.api;

import com.chandan.services.MovieService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MovieController {
    final private MovieService movieService;
    public String createMovie(String movieName) {
        return movieService.createMovie(movieName).getId();
    }
}

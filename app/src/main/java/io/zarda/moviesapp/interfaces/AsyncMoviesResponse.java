package io.zarda.moviesapp.interfaces;

import java.util.List;

import io.zarda.moviesapp.models.Movie;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public interface AsyncMoviesResponse {
    void onFetchingMoviesFinish(List<Movie> result);
}

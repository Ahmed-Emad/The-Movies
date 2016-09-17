package io.zarda.moviesapp.interfaces;

import java.util.ArrayList;

import io.zarda.moviesapp.models.Movie;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public interface AsyncMoviesResponse {
    void onFetchingMoviesFinish(ArrayList<Movie> result);
}

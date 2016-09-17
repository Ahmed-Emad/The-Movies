package io.zarda.moviesapp.interfaces;

import io.zarda.moviesapp.models.MovieTrailers;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */

public interface AsyncTrailersResponse {
    void onFetchingTrailersFinish(MovieTrailers result);
}

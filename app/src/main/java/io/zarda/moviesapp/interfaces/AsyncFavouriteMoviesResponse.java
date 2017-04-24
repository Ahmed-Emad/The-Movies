package io.zarda.moviesapp.interfaces;

import android.database.Cursor;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public interface AsyncFavouriteMoviesResponse {
    void onFetchingFavouriteMoviesFinish(Cursor result);
}

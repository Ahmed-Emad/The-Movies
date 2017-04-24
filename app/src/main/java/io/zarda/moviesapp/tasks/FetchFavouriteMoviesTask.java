package io.zarda.moviesapp.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import io.zarda.moviesapp.data.MoviesContract;
import io.zarda.moviesapp.interfaces.AsyncFavouriteMoviesResponse;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class FetchFavouriteMoviesTask extends AsyncTask<Void, Void, Cursor> {

    private AsyncFavouriteMoviesResponse delegate;
    private Context context;

    public FetchFavouriteMoviesTask(AsyncFavouriteMoviesResponse delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected Cursor doInBackground(Void... params) {
        return context.getContentResolver().query(
                MoviesContract.MovieEntry.MOVIES_CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    protected void onPostExecute(Cursor result) {
        delegate.onFetchingFavouriteMoviesFinish(result);
    }
}
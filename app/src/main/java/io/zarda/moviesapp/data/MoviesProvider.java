package io.zarda.moviesapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import io.zarda.moviesapp.data.MoviesContract.MovieEntry;
import io.zarda.moviesapp.data.MoviesContract.TrailerEntry;
import io.zarda.moviesapp.models.Movie;
import io.zarda.moviesapp.models.MovieTrailers;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class MoviesProvider {

    private Context context;
    private MoviesDBHelper moviesDBHelper;

    public MoviesProvider(Context context) {
        this.context = context;
        moviesDBHelper = new MoviesDBHelper(context);
    }

    public ArrayList<Movie> getFavouriteMovies() {
        SQLiteDatabase db = moviesDBHelper.getReadableDatabase();
        ArrayList<Movie> movies = new ArrayList<>();
        Cursor cursor = db.query(MovieEntry.TABLE_NAME, null, null, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Movie movie = new Movie(
                    cursor.getLong(cursor.getColumnIndex(MovieEntry.COLUMN_ID)),
                    null,
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP_PATH)),
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE)),
                    cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_LANGUAGE)),
                    cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_POPULARITY)),
                    cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE)),
                    cursor.getLong(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_COUNT)),
                    null,
                    null,
                    null
            );
            movies.add(movie);
        }
        return movies;
    }

    public MovieTrailers getMovieTrailers(String movieId) {
        SQLiteDatabase db = moviesDBHelper.getReadableDatabase();
        ArrayList<MovieTrailers.Video> videos = new ArrayList<>();
        Cursor cursor = db.query(TrailerEntry.TABLE_NAME, null,
                TrailerEntry.COLUMN_MOVIE_KEY + "=" + movieId, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            MovieTrailers.Video video = new MovieTrailers.Video(
                    null,
                    cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_KEY)),
                    cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_NAME)),
                    null,
                    null,
                    null
            );
            videos.add(video);
        }
        MovieTrailers trailers = new MovieTrailers(null, videos);
        return trailers;
    }

    public boolean isFavouriteMovie(Movie movie) {
        SQLiteDatabase db = moviesDBHelper.getReadableDatabase();
        Cursor cursor = db.query(MovieEntry.TABLE_NAME, null,
                MovieEntry.COLUMN_ID + "=" + movie.getId(), null, null, null, null);
        return cursor.getCount() > 0;
    }

    public long insertFavouriteMovie(Movie movie, MovieTrailers trailers) {
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();
        for (MovieTrailers.Video video : trailers.getVideos()) {
            ContentValues trailersValues = new ContentValues();
            trailersValues.put(TrailerEntry.COLUMN_MOVIE_KEY, movie.getId());
            trailersValues.put(TrailerEntry.COLUMN_KEY, video.getKey());
            trailersValues.put(TrailerEntry.COLUMN_NAME, video.getName());
            db.insert(MoviesContract.TrailerEntry.TABLE_NAME, null, trailersValues);
        }
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieEntry.COLUMN_ID, movie.getId());
        movieValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
        movieValues.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdrop_path());
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        movieValues.put(MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginal_language());
        movieValues.put(MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        movieValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        movieValues.put(MovieEntry.COLUMN_VOTE_COUNT, movie.getVote_count());
        return db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, movieValues);
    }

    public int deleteFavouriteMovie(Movie movie) {
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();
        db.delete(TrailerEntry.TABLE_NAME, TrailerEntry.COLUMN_MOVIE_KEY + "=" + movie.getId(), null);
        return db.delete(MovieEntry.TABLE_NAME, MovieEntry.COLUMN_ID + "=" + movie.getId(), null);
    }

}

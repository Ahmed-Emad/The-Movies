package io.zarda.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import io.zarda.moviesapp.data.MoviesContract.MovieEntry;
import io.zarda.moviesapp.models.Movie;
import io.zarda.moviesapp.models.MovieTrailers;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class Utils {

    public static String MOVIES_APP_KEY = "2fb851b2ffe827556aa102a807e3e6e2";

    public static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w780/";

    public static String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w780/";

    public static String stringJoin(String sSep, List<String> aArr) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.size(); i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr.get(i));
        }
        return sbStr.toString();
    }

    public static boolean isFavouriteMovie(Context context, Movie movie) {
        String stringId = Long.toString(movie.getId());
        Uri uri = MovieEntry.MOVIES_CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        Movie checkMovie = null;
        if (cursor != null && cursor.getCount() > 0) {
            checkMovie = new Movie(cursor);
        }
        return checkMovie != null;
    }

    private static void insertFavouriteMovie(Context context, Movie movie) {
        ContentValues movieContentValues = new ContentValues();
        movieContentValues.put(MovieEntry.COLUMN_ID, movie.getId());
        movieContentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieContentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
        movieContentValues.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdrop_path());
        movieContentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieContentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        movieContentValues.put(MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginal_language());
        movieContentValues.put(MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        movieContentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        movieContentValues.put(MovieEntry.COLUMN_VOTE_COUNT, movie.getVote_count());
        context.getContentResolver().insert(MovieEntry.MOVIES_CONTENT_URI, movieContentValues);
    }

    public static void insertFavouriteMovie(Context context, Movie movie, MovieTrailers trailers) {
        insertFavouriteMovie(context, movie);
    }

    public static ArrayList<Movie> getFavouriteMovies(Context context) {
        ArrayList<Movie> movies = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MovieEntry.MOVIES_CONTENT_URI,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            movies.add(new Movie(cursor));
        }
        return movies;
    }

    public static void deleteFavouriteMovie(Context context, Movie movie) {
        String movieId = Long.toString(movie.getId());
        Uri movieUri = MovieEntry.MOVIES_CONTENT_URI;
        movieUri = movieUri.buildUpon().appendPath(movieId).build();
        context.getContentResolver().delete(movieUri, null, null);
    }

}

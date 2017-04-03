package io.zarda.moviesapp.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import io.zarda.moviesapp.Utils;
import io.zarda.moviesapp.data.MoviesProvider;
import io.zarda.moviesapp.fragments.MoviesFragment;
import io.zarda.moviesapp.interfaces.AsyncMoviesResponse;
import io.zarda.moviesapp.models.Movie;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    final static String PARAM_API_KEY = "api_key";
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private AsyncMoviesResponse delegate;
    private Context context;

    public FetchMoviesTask(AsyncMoviesResponse delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
    }

    private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(moviesJsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        ArrayList<Movie> movies = new ArrayList<Movie>();
        for (int i = 0; i < jsonArray.length(); i++) {
            movies.add(new Movie(jsonArray.getJSONObject(i)));
        }
        return movies;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        if (params.length < 1) {
            return null;
        }

        String movies_type = params[0];

        if (movies_type == MoviesFragment.FAVOURITES_KEY) {
            return (new MoviesProvider(context)).getFavouriteMovies();
        }

        HttpURLConnection urlConnection = null;

        String moviesJsonStr = null;


        final String MOVIES_LIST_BASE_URL =
                "http://api.themoviedb.org/3/movie/" + movies_type;
        Uri builtUri = Uri.parse(MOVIES_LIST_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, Utils.MOVIES_APP_KEY)
                .build();

        try {
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                moviesJsonStr = scanner.next();
            } else {
                return null;
            }

            Log.v(LOG_TAG, "string: " + moviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        try {
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {
        delegate.onFetchingMoviesFinish(result);
    }
}
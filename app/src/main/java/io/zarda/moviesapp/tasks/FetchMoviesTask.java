package io.zarda.moviesapp.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.zarda.moviesapp.Utils;
import io.zarda.moviesapp.data.MoviesProvider;
import io.zarda.moviesapp.fragments.MoviesFragment;
import io.zarda.moviesapp.interfaces.AsyncMoviesResponse;
import io.zarda.moviesapp.models.Movie;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

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
        BufferedReader reader = null;

        String moviesJsonStr = null;


        try {
            final String MOVIES_LIST_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + movies_type + "?api_key=" + Utils.MOVIES_APP_KEY;

            Uri builtUri = Uri.parse(MOVIES_LIST_BASE_URL);
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            moviesJsonStr = buffer.toString();

            Log.v(LOG_TAG, "string: " + moviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
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
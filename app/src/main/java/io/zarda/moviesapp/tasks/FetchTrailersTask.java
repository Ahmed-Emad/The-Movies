package io.zarda.moviesapp.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.zarda.moviesapp.Utils;
import io.zarda.moviesapp.data.MoviesProvider;
import io.zarda.moviesapp.fragments.DetailsFragment;
import io.zarda.moviesapp.interfaces.AsyncTrailersResponse;
import io.zarda.moviesapp.models.MovieTrailers;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class FetchTrailersTask extends AsyncTask<String, Void, MovieTrailers> {

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    private AsyncTrailersResponse delegate;
    private Context context;

    public FetchTrailersTask(AsyncTrailersResponse delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
    }

    private MovieTrailers getMovieTrailersFromJson(String movieJsonStr)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(movieJsonStr);
        MovieTrailers movieTrailers = new MovieTrailers(jsonObject);
        return movieTrailers;
    }

    @Override
    protected MovieTrailers doInBackground(String... params) {

        if (params.length < 1) {
            return null;
        }

        String movie_id = params[0];
        String type = params[1];

        if (type == DetailsFragment.FAVOURITES_KEY) {
            return (new MoviesProvider(context)).getMovieTrailers(movie_id);
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;


        try {
            final String MOVIE_TRAILERS_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + movie_id + "/videos?api_key=" + Utils.MOVIES_APP_KEY;

            Uri builtUri = Uri.parse(MOVIE_TRAILERS_BASE_URL);
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
            movieJsonStr = buffer.toString();

            Log.v(LOG_TAG, "string: " + movieJsonStr);
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
            return getMovieTrailersFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(MovieTrailers result) {
        delegate.onFetchingTrailersFinish(result);
    }

}
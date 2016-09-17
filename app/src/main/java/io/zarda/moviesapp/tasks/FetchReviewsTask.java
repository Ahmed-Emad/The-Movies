package io.zarda.moviesapp.tasks;

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
import io.zarda.moviesapp.interfaces.AsyncReviewsResponse;
import io.zarda.moviesapp.models.MovieReviews;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class FetchReviewsTask extends AsyncTask<String, Void, MovieReviews> {

    private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    private AsyncReviewsResponse delegate;

    public FetchReviewsTask(AsyncReviewsResponse delegate) {
        this.delegate = delegate;
    }

    private MovieReviews getMovieReviewsFromJson(String movieJsonStr)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(movieJsonStr);
        MovieReviews movieReviews = new MovieReviews(jsonObject);
        return movieReviews;
    }

    @Override
    protected MovieReviews doInBackground(String... params) {

        if (params.length < 1) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        String movie_id = params[0];

        try {
            final String MOVIE_REVIEWS_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + movie_id + "/reviews?api_key=" + Utils.MOVIES_APP_KEY;

            Uri builtUri = Uri.parse(MOVIE_REVIEWS_BASE_URL);
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
            return getMovieReviewsFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(MovieReviews result) {
        delegate.onFetchingReviewsFinish(result);
    }

}
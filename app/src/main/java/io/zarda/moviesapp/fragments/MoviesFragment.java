package io.zarda.moviesapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.List;

import io.zarda.moviesapp.R;
import io.zarda.moviesapp.activities.DetailsActivity;
import io.zarda.moviesapp.activities.MainActivity;
import io.zarda.moviesapp.adapters.MoviesAdapter;
import io.zarda.moviesapp.adapters.MoviesCursorAdapter;
import io.zarda.moviesapp.interfaces.AsyncFavouriteMoviesResponse;
import io.zarda.moviesapp.interfaces.AsyncMoviesResponse;
import io.zarda.moviesapp.interfaces.MovieClickListener;
import io.zarda.moviesapp.models.Movie;
import io.zarda.moviesapp.tasks.FetchFavouriteMoviesTask;
import io.zarda.moviesapp.tasks.FetchMoviesTask;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class MoviesFragment extends Fragment implements AsyncMoviesResponse, MovieClickListener,
        AsyncFavouriteMoviesResponse, AdapterView.OnItemSelectedListener {

    public static final String POPULAR_KEY = "popular";
    public static final String RATED_KEY = "top_rated";
    public static final String POSITION_KEY = "selected_position";
    public static boolean inFavourites = false;

    List<Movie> movies;
    GridView gridView;
    View lastView;

    private int currentSelectedPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview);

        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            currentSelectedPosition = savedInstanceState.getInt(POSITION_KEY);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_fragment, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.movies_options, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        spinner.setSelection(currentSelectedPosition);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchFavourites();
    }

    public void fetchFavourites() {
        if (inFavourites) {
            FetchFavouriteMoviesTask moviesTask = new FetchFavouriteMoviesTask(this, getActivity());
            moviesTask.execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putInt(POSITION_KEY, currentSelectedPosition);
        System.out.println("putInt currentSelectedPosition: " + currentSelectedPosition);
        super.onSaveInstanceState(state);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentSelectedPosition = position;
        if (position == 0) {
            fetchPopularMovies();
        } else if (position == 1) {
            fetchRatedMovies();
        } else {
            fetchFavouriteMovies();
        }
    }

    private void fetchFavouriteMovies() {
        FetchFavouriteMoviesTask moviesTask = new FetchFavouriteMoviesTask(this, getActivity());
        moviesTask.execute();
        inFavourites = true;
    }

    private void fetchRatedMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask(this, getActivity());
        moviesTask.execute(RATED_KEY);
        inFavourites = false;
    }

    private void fetchPopularMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask(this, getActivity());
        moviesTask.execute(POPULAR_KEY);
        inFavourites = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void OnMovieClickListener(Movie movie) {
        if (MainActivity.TWO_PANE) {
            Bundle args = new Bundle();
            args.putParcelable(DetailsFragment.MOVIE_KEY, movie);
            DetailsFragment detailFragment = new DetailsFragment();
            detailFragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.details_container, detailFragment, MainActivity.TAG_DETAILS_FRAGMENT)
                    .commit();
        } else {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra(DetailsFragment.MOVIE_KEY, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onFetchingMoviesFinish(List<Movie> result) {
        movies = result;
        MoviesAdapter moviesAdapter = new MoviesAdapter(getContext(), movies);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movies.get(position);
                if (MainActivity.TWO_PANE) {
                    if (lastView != null) {
                        lastView.setPadding(0, 0, 0, 0);
                    }
                    lastView = view.findViewById(R.id.img_poster);
                    lastView.setPadding(0, 10, 0, 10);
                }
                OnMovieClickListener(movie);
            }
        });
    }

    @Override
    public void onFetchingFavouriteMoviesFinish(final Cursor result) {
        movies = null;
        MoviesCursorAdapter moviesAdapter = new MoviesCursorAdapter(getContext(), result);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                result.moveToPosition(position);
                Movie movie = new Movie(result);
                if (MainActivity.TWO_PANE) {
                    if (lastView != null) {
                        lastView.setPadding(0, 0, 0, 0);
                    }
                    lastView = view.findViewById(R.id.img_poster);
                    lastView.setPadding(0, 10, 0, 10);
                }
                OnMovieClickListener(movie);
            }
        });
    }
}

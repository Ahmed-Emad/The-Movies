package io.zarda.moviesapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.zarda.moviesapp.R;
import io.zarda.moviesapp.activities.MainActivity;
import io.zarda.moviesapp.adapters.ReviewsAdapter;
import io.zarda.moviesapp.adapters.TrailersAdapter;
import io.zarda.moviesapp.data.MoviesProvider;
import io.zarda.moviesapp.interfaces.AsyncReviewsResponse;
import io.zarda.moviesapp.interfaces.AsyncTrailersResponse;
import io.zarda.moviesapp.models.Movie;
import io.zarda.moviesapp.models.MovieReviews;
import io.zarda.moviesapp.models.MovieTrailers;
import io.zarda.moviesapp.tasks.FetchReviewsTask;
import io.zarda.moviesapp.tasks.FetchTrailersTask;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class DetailsFragment extends Fragment implements AsyncTrailersResponse,
        AsyncReviewsResponse {

    public static final String MOVIE_KEY = "MOVIE_KEY";
    public static final String THE_MOVIE_KEY = "the_movie";
    public static final String FAVOURITES_KEY = "favourites";
    private final String LOG_TAG = DetailsFragment.class.getSimpleName();
    private MoviesProvider moviesProvider;

    private Movie movie;
    private MovieTrailers trailers;

    private TextView tvTitle, tvOverview, tvRelease_date, tvOriginalLanguage, tvVoteAverage,
            tvTrailers, tvReviews, tvFavourite, tvShare;
    private ImageView imgPoster, imgBackdrop, imgFavourite, imgShare;
    private LinearLayout llFavourite, llShare;
    private RecyclerView rvTrailers, rvReviews;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;
    private FetchTrailersTask trailersTask;
    private FetchReviewsTask reviewsTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTrailers = (TextView) rootView.findViewById(R.id.tv_trailers);
        tvReviews = (TextView) rootView.findViewById(R.id.tv_reviews);
        tvRelease_date = (TextView) rootView.findViewById(R.id.tv_release_date);
        tvOriginalLanguage = (TextView) rootView.findViewById(R.id.tv_original_language);
        tvVoteAverage = (TextView) rootView.findViewById(R.id.tv_vote_average);
        tvOverview = (TextView) rootView.findViewById(R.id.tv_overview);
        tvFavourite = (TextView) rootView.findViewById(R.id.tv_favourite);
        tvShare = (TextView) rootView.findViewById(R.id.tv_share);
        imgPoster = (ImageView) rootView.findViewById(R.id.img_poster);
        imgBackdrop = (ImageView) rootView.findViewById(R.id.img_backdrop);
        imgFavourite = (ImageView) rootView.findViewById(R.id.img_favourite);
        imgShare = (ImageView) rootView.findViewById(R.id.img_share);
        rvTrailers = (RecyclerView) rootView.findViewById(R.id.rv_trailers);
        rvReviews = (RecyclerView) rootView.findViewById(R.id.rv_reviews);
        llFavourite = (LinearLayout) rootView.findViewById(R.id.ll_favourite);
        llShare = (LinearLayout) rootView.findViewById(R.id.ll_share);

        if (getArguments() != null) {
            movie = getArguments().getParcelable(MOVIE_KEY);
            setupViews();
        } else if (getActivity().getIntent().getParcelableExtra(MOVIE_KEY) != null) {
            movie = getActivity().getIntent().getParcelableExtra(MOVIE_KEY);
            setupViews();
        }

        return rootView;
    }

    private void setupViews() {
        tvTrailers.setVisibility(View.VISIBLE);
        tvReviews.setVisibility(View.VISIBLE);
        rvReviews.setVisibility(View.VISIBLE);
        rvTrailers.setVisibility(View.VISIBLE);
        tvTitle.setText(movie.getTitle());
        tvRelease_date.setText(movie.getRelease_date());
        tvOriginalLanguage.setText(movie.getOriginal_language());
        tvVoteAverage.setText((movie.getVote_average() + "/10"));
        tvOverview.setText(movie.getOverview());
        tvShare.setText("Share");

        Glide.clear(imgPoster);
        Glide.clear(imgBackdrop);
        Glide.with(getContext()).load(movie.getPoster_path()).into(imgPoster);
        Glide.with(getContext()).load(movie.getBackdrop_path()).into(imgBackdrop);
        Glide.with(getContext()).load(R.drawable.ic_share_black_48dp).into(imgShare);

        moviesProvider = new MoviesProvider(getActivity());
        trailersTask = new FetchTrailersTask(this, getActivity());
        if (moviesProvider.isFavouriteMovie(movie)) {
            trailersTask.execute(String.valueOf(movie.getId()), FAVOURITES_KEY);
            Glide.with(getContext()).load(R.drawable.ic_favorite_black_48dp).into(imgFavourite);
            tvFavourite.setText("Unlike");
        } else {
            trailersTask.execute(String.valueOf(movie.getId()), THE_MOVIE_KEY);
            Glide.with(getContext()).load(R.drawable.ic_favorite_border_black_48dp).into(imgFavourite);
            tvFavourite.setText("Like");
        }
        llFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavourite();
            }
        });
        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = movie.getTitle();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "The Movies");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        reviewsTask = new FetchReviewsTask(this);
        reviewsTask.execute(String.valueOf(movie.getId()));
    }

    private void toggleFavourite() {
        if (moviesProvider.isFavouriteMovie(movie)) {
            moviesProvider.deleteFavouriteMovie(movie);
            if (MainActivity.TWO_PANE) {
                ((MoviesFragment) getActivity().getSupportFragmentManager().
                        findFragmentById(R.id.fragment_movies)).fetchFavourites();
            }
        } else {
            moviesProvider.insertFavouriteMovie(movie, trailers);
            if (MainActivity.TWO_PANE) {
                ((MoviesFragment) getActivity().getSupportFragmentManager().
                        findFragmentById(R.id.fragment_movies)).fetchFavourites();
            }
        }
        if (moviesProvider.isFavouriteMovie(movie)) {
            Glide.with(getContext()).load(R.drawable.ic_favorite_black_48dp).into(imgFavourite);
            tvFavourite.setText("Unlike");
        } else {
            Glide.with(getContext()).load(R.drawable.ic_favorite_border_black_48dp).into(imgFavourite);
            tvFavourite.setText("Like");
        }
    }

    @Override
    public void onFetchingTrailersFinish(MovieTrailers result) {
        if (result != null) {
            Log.w(LOG_TAG, result.toString());
            trailers = result;
            trailersAdapter = new TrailersAdapter(getActivity(), result.getVideos());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rvTrailers.setLayoutManager(mLayoutManager);
            rvTrailers.setItemAnimator(new DefaultItemAnimator());
            rvTrailers.setAdapter(trailersAdapter);
        }
    }

    @Override
    public void onFetchingReviewsFinish(MovieReviews output) {
        if (output != null) {
            Log.w(LOG_TAG, output.toString());
            reviewsAdapter = new ReviewsAdapter(getActivity(), output.getReviews());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rvReviews.setLayoutManager(mLayoutManager);
            rvReviews.setItemAnimator(new DefaultItemAnimator());
            rvReviews.setAdapter(reviewsAdapter);
        }
    }

}

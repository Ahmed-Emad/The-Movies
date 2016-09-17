package io.zarda.moviesapp.interfaces;

import io.zarda.moviesapp.models.MovieReviews;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */

public interface AsyncReviewsResponse {
    void onFetchingReviewsFinish(MovieReviews result);
}

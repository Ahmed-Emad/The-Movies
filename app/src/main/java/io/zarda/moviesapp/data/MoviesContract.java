package io.zarda.moviesapp.data;

import android.provider.BaseColumns;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class MoviesContract {

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
    }

    public static final class TrailerEntry implements BaseColumns {

        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_NAME = "trailer_name";
        public static final String COLUMN_KEY = "trailer_key";

        public static final String COLUMN_MOVIE_KEY = "movie_id";

    }

}


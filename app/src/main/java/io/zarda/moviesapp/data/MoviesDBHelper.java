package io.zarda.moviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.zarda.moviesapp.data.MoviesContract.MovieEntry;
import io.zarda.moviesapp.data.MoviesContract.TrailerEntry;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 2;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieEntry.TABLE_NAME +
                " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                MovieEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +

                " UNIQUE (" + MovieEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TrailerEntry.TABLE_NAME +
                " (" +

                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + TrailerEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_ID + ")" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILERS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}

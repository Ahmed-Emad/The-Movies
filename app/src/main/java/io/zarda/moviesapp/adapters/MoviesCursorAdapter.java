package io.zarda.moviesapp.adapters;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.zarda.moviesapp.R;
import io.zarda.moviesapp.models.Movie;

public class MoviesCursorAdapter extends BaseAdapter {

    private Context mContext;
    private Cursor moviesCursor;

    public MoviesCursorAdapter(Context context, Cursor moviesCursor) {
        mContext = context;
        this.moviesCursor = moviesCursor;
    }

    @Override
    public int getCount() {
        if (moviesCursor != null) {
            return moviesCursor.getCount();
        } else {
            return 0;
        }
    }

    public Movie getItem(int position) {
        if (moviesCursor.moveToPosition(position)) {
            return new Movie(moviesCursor);
        }
        return null;
    }

    public long getItemId(int position) {
        Movie movie = getItem(position);
        if (movie != null) {
            return movie.getId();
        }
        return 0;
    }

    public void swapCursor(Cursor newCursor) {
        moviesCursor = newCursor;
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setId(R.id.img_poster);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setBackgroundColor(mContext.getResources().getColor(R.color.style_color_accent));
        } else {
            imageView = (ImageView) convertView;
        }

        Glide.clear(imageView);
        Glide.with(mContext).load(getItem(position).getPoster_path()).into(imageView);
        return imageView;
    }
}

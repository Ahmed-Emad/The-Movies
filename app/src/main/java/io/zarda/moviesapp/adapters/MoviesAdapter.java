package io.zarda.moviesapp.adapters;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.zarda.moviesapp.R;
import io.zarda.moviesapp.models.Movie;

public class MoviesAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Movie> movies;

    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        mContext = context;
        this.movies = movies;
    }

    public int getCount() {
        if (movies != null) {
            return movies.size();
        } else {
            return 0;
        }
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setId(R.id.img_poster);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 600));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setBackgroundColor(mContext.getResources().getColor(R.color.style_color_accent));
        } else {
            imageView = (ImageView) convertView;
        }

        Glide.clear(imageView);
        Glide.with(mContext).load(movies.get(position).getPoster_path()).into(imageView);
        return imageView;
    }
}
package io.zarda.moviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.zarda.moviesapp.R;
import io.zarda.moviesapp.models.MovieReviews;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<MovieReviews.Review> reviews;
    private Context mContext;

    public ReviewsAdapter(Context mContext, List<MovieReviews.Review> reviews) {
        this.reviews = reviews;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MovieReviews.Review review = reviews.get(position);
        holder.tvAuthor.setText(review.getAuthor() + ":");
        holder.tvContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAuthor, tvContent;

        public ViewHolder(View view) {
            super(view);
            tvAuthor = (TextView) view.findViewById(R.id.tv_review_author);
            tvContent = (TextView) view.findViewById(R.id.tv_review_content);
        }
    }

}

package io.zarda.moviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import io.zarda.moviesapp.R;
import io.zarda.moviesapp.models.MovieTrailers;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private List<MovieTrailers.Video> videos;
    private Context mContext;

    public TrailersAdapter(Context mContext, List<MovieTrailers.Video> videos) {
        this.videos = videos;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trailer, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MovieTrailers.Video video = videos.get(position);
        holder.btnTrailer.setText(video.getName());
        holder.btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("vnd.youtube://" + video.getKey()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button btnTrailer;

        public ViewHolder(View view) {
            super(view);
            btnTrailer = (Button) view.findViewById(R.id.btn_video);
        }
    }

}

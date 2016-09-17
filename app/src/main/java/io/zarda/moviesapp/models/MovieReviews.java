package io.zarda.moviesapp.models;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.zarda.moviesapp.Utils;


public class MovieReviews implements Parcelable {

    public static final Creator<MovieReviews> CREATOR = new Creator<MovieReviews>() {
        @Override
        public MovieReviews createFromParcel(Parcel in) {
            return new MovieReviews(in);
        }

        @Override
        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };

    private Long id;
    private ArrayList<Review> reviews;

    public MovieReviews() {
    }

    public MovieReviews(Parcel in) {
        this.id = in.readLong();
        if (this.reviews == null) {
            this.reviews = new ArrayList<Review>();
        }
        in.readList(this.reviews, null);
    }

    public MovieReviews(Long id, ArrayList<Review> reviews) {
        this.id = id;
        this.reviews = reviews;
    }

    public MovieReviews(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            this.reviews = new ArrayList<Review>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject arrayJsonObject = jsonArray.getJSONObject(i);
                this.reviews.add(new Review(arrayJsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Creator<MovieReviews> getCREATOR() {
        return CREATOR;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeList(this.reviews);
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        list.add(getId() + ": ");
        for (Review v : reviews) {
            list.add(v.getId());
            list.add(v.getAuthor());
            list.add(v.getContent());
            list.add(" - ");
        }
        return Utils.stringJoin(", ", list);
    }

    public static class Review implements Parcelable {
        public static final Creator<Review> CREATOR = new Creator<Review>() {
            @Override
            public Review createFromParcel(Parcel in) {
                return new Review(in);
            }

            @Override
            public Review[] newArray(int size) {
                return new Review[size];
            }
        };

        private String id;
        private String author;
        private String content;

        public Review() {
        }

        public Review(Parcel in) {
            this.id = in.readString();
            this.author = in.readString();
            this.content = in.readString();
        }

        public Review(String id, String author, String content) {
            this.id = id;
            this.author = author;
            this.content = content;
        }

        public Review(JSONObject jsonObject) {
            try {
                this.id = jsonObject.getString("id");
                this.author = jsonObject.getString("author");
                this.content = jsonObject.getString("content");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public static Creator<Review> getCREATOR() {
            return CREATOR;
        }

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.author);
            dest.writeString(this.content);
        }
    }

}

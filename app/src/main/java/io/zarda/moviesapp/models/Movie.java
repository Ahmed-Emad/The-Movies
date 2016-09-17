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

import io.zarda.moviesapp.Utils;


public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private Long id;
    private String original_title;
    private String title;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private String release_date;
    private String original_language;
    private Double popularity;
    private Double vote_average;
    private Long vote_count;
    private ArrayList<Integer> genre_ids;
    private Boolean video;
    private Boolean adult;

    public Movie() {
    }

    public Movie(Parcel in) {
        this.id = in.readLong();
        this.original_title = in.readString();
        this.title = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.original_language = in.readString();
        this.popularity = in.readDouble();
        this.vote_average = in.readDouble();
        this.vote_count = in.readLong();
        if (this.genre_ids == null) {
            this.genre_ids = new ArrayList<Integer>();
        }
        in.readList(this.genre_ids, null);
        this.video = in.readByte() != 0;
        this.adult = in.readByte() != 0;
    }

    public Movie(Long id, String original_title, String title, String poster_path,
                 String backdrop_path, String overview, String release_date,
                 String original_language, Double popularity, Double vote_average,
                 Long vote_count, ArrayList<Integer> genre_ids, Boolean video, Boolean adult) {
        this.id = id;
        this.original_title = original_title;
        this.title = title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;
        this.original_language = original_language;
        this.popularity = popularity;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.genre_ids = genre_ids;
        this.video = video;
        this.adult = adult;
    }

    public Movie(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            this.original_title = jsonObject.getString("original_title");
            this.title = jsonObject.getString("title");
            this.poster_path = Utils.POSTER_BASE_URL + jsonObject.getString("poster_path");
            this.backdrop_path = Utils.BACKDROP_BASE_URL + jsonObject.getString("backdrop_path");
            this.overview = jsonObject.getString("overview");
            this.release_date = jsonObject.getString("release_date");
            this.original_language = jsonObject.getString("original_language");
            this.popularity = jsonObject.getDouble("popularity");
            this.vote_average = jsonObject.getDouble("vote_average");
            this.vote_count = jsonObject.getLong("vote_count");

            JSONArray jsonArray = jsonObject.getJSONArray("genre_ids");
            this.genre_ids = new ArrayList<Integer>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                this.genre_ids.add(jsonArray.getInt(i));
            }

            this.video = jsonObject.getBoolean("video");
            this.adult = jsonObject.getBoolean("adult");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Creator<Movie> getCREATOR() {
        return CREATOR;
    }

    public Long getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public Long getVote_count() {
        return vote_count;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public Boolean getVideo() {
        return video;
    }

    public Boolean getAdult() {
        return adult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.title);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeString(this.original_language);
        dest.writeDouble(this.popularity);
        dest.writeDouble(this.vote_average);
        dest.writeLong(this.vote_count);
        dest.writeList(this.genre_ids);
        if (this.video != null) {
            dest.writeByte((byte) (this.video ? 1 : 0));
        }
        if (this.adult != null) {
            dest.writeByte((byte) (this.adult ? 1 : 0));
        }
    }

}
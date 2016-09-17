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


public class MovieTrailers implements Parcelable {

    public static final Creator<MovieTrailers> CREATOR = new Creator<MovieTrailers>() {
        @Override
        public MovieTrailers createFromParcel(Parcel in) {
            return new MovieTrailers(in);
        }

        @Override
        public MovieTrailers[] newArray(int size) {
            return new MovieTrailers[size];
        }
    };

    private Long id;
    private ArrayList<Video> videos;

    public MovieTrailers() {
    }

    public MovieTrailers(Parcel in) {
        this.id = in.readLong();
        if (this.videos == null) {
            this.videos = new ArrayList<Video>();
        }
        in.readList(this.videos, null);
    }

    public MovieTrailers(Long id, ArrayList<Video> videos) {
        this.id = id;
        this.videos = videos;
    }

    public MovieTrailers(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getLong("id");
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            this.videos = new ArrayList<Video>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject arrayJsonObject = jsonArray.getJSONObject(i);
                this.videos.add(new Video(arrayJsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Creator<MovieTrailers> getCREATOR() {
        return CREATOR;
    }

    public ArrayList<Video> getVideos() {
        return videos;
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
        dest.writeList(this.videos);
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        list.add(getId() + ": ");
        for (Video v : videos) {
            list.add(v.getId());
            list.add(v.getKey());
            list.add(v.getName());
            list.add(v.getSite());
            list.add(v.getSize() + "");
            list.add(v.getType());
            list.add(" - ");
        }
        return Utils.stringJoin(", ", list);
    }

    public static class Video implements Parcelable {
        public static final Creator<Video> CREATOR = new Creator<Video>() {
            @Override
            public Video createFromParcel(Parcel in) {
                return new Video(in);
            }

            @Override
            public Video[] newArray(int size) {
                return new Video[size];
            }
        };
        private String id;
        private String key;
        private String name;
        private String site;
        private Integer size;
        private String type;

        public Video() {
        }

        public Video(Parcel in) {
            this.id = in.readString();
            this.key = in.readString();
            this.name = in.readString();
            this.site = in.readString();
            this.size = in.readInt();
            this.type = in.readString();
        }

        public Video(String id, String key, String name, String site, Integer size, String type) {
            this.id = id;
            this.key = key;
            this.name = name;
            this.site = site;
            this.size = size;
            this.type = type;
        }

        public Video(JSONObject jsonObject) {
            try {
                this.id = jsonObject.getString("id");
                this.key = jsonObject.getString("key");
                this.name = jsonObject.getString("name");
                this.site = jsonObject.getString("site");
                this.size = jsonObject.getInt("size");
                this.type = jsonObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public static Creator<Video> getCREATOR() {
            return CREATOR;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public Integer getSize() {
            return size;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.key);
            dest.writeString(this.name);
            dest.writeString(this.site);
            dest.writeInt(this.size);
            dest.writeString(this.type);
        }
    }

}

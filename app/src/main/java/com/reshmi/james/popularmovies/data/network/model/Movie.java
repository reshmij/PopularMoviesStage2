package com.reshmi.james.popularmovies.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

@SuppressWarnings("CanBeFinal")
public class Movie implements Parcelable {

    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("genre_ids")
    private long[] genreIds;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("video")
    private boolean video;
    @SerializedName("popularity")
    private double popularity;

    @SuppressWarnings("unused")
    public Movie(float voteAverage, String backdropPath, boolean adult, long id, String title, String overview, String originalLanguage, long[] genreIds, String releaseDate, String originalTitle, int voteCount, String posterPath, boolean video, double popularity) {
        this.voteAverage = voteAverage;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.originalLanguage = originalLanguage;
        this.genreIds = genreIds;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.voteCount = voteCount;
        this.posterPath = posterPath;
        this.video = video;
        this.popularity = popularity;
    }

    public Movie(int id, String title, String posterPath, String overview, float voteAverage, String releaseDate) {

        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }


    public float getVoteAverage() {
        return voteAverage;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "voteAverage='" + voteAverage + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", adult='" + adult + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", genreIds=" + Arrays.toString(genreIds) +
                ", releaseDate='" + releaseDate + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", voteCount='" + voteCount + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", video='" + video + '\'' +
                ", popularity='" + popularity + '\'' +
                '}';
    }

    public Movie(String originalTitle, String posterPath) {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
    }

    private Movie(Parcel in) {
        voteAverage = in.readFloat();
        backdropPath = in.readString();
        adult = in.readByte() != 0x00;
        id = in.readLong();
        title = in.readString();
        overview = in.readString();
        originalLanguage = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        voteCount = in.readInt();
        posterPath = in.readString();
        video = in.readByte() != 0x00;
        popularity = in.readDouble();
        genreIds = in.createLongArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(voteAverage);
        dest.writeString(backdropPath);
        dest.writeByte((byte) (adult ? 0x01 : 0x00));
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(originalLanguage);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeInt(voteCount);
        dest.writeString(posterPath);
        dest.writeByte((byte) (video ? 0x01 : 0x00));
        dest.writeDouble(popularity);
        dest.writeLongArray(genreIds);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
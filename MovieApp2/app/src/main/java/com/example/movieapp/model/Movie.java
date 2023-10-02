package com.example.movieapp.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Comparable, Cloneable, Parcelable {

    public static final int TYPE_GRID = 1;
    public static final int TYPE_LIST = 2;

    @SerializedName("id")
    private Integer idMovie;

    @SerializedName("adult")
    private Boolean isAdultMovie;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("title")
    private String title;

    @SerializedName("vote_average")
    private Double voteAverage;

//    @SerializedName("backdrop_path")
//    private String backdropPath;
//
//    @SerializedName("genre_ids")
//    private List<Integer> genreIds = null;
//
//    @SerializedName("original_language")
//    private String originalLanguage;
//
//    @SerializedName("original_title")
//    private String originalTitle;
//
//    @SerializedName("vote_count")
//    private Integer voteCount;
//
//    @SerializedName("video")
//    private Boolean video;
//
//    @SerializedName("popularity")
//    private Double popularity;

    private boolean isFavouriteMovie;
    private String reminderDateTime;
    private int typeDisplay;

    public Movie(Integer idMovie, Boolean isAdultMovie, String overview, String posterPath
            , String releaseDate, String title, Double voteAverage, boolean isFavouriteMovie
            , String reminderDateTime, int typeDisplay) {
        this.idMovie = idMovie;
        this.isAdultMovie = isAdultMovie;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.voteAverage = voteAverage;
        this.isFavouriteMovie = isFavouriteMovie;
        this.reminderDateTime = reminderDateTime;
        this.typeDisplay = typeDisplay;
    }

    public Movie(int idMovie, String title, String posterPath, String releaseDate, double voteAverage, boolean isAdultMovie
            , boolean isFavouriteMovie, String overview) {
        this.idMovie = idMovie;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.isAdultMovie = isAdultMovie;
        this.isFavouriteMovie = isFavouriteMovie;
        this.overview = overview;
    }

    public Movie(int idMovie, String title, String posterPath, String releaseDate, double voteAverage, boolean isAdultMovie, boolean isFavouriteMovie, String overview
            , String reminderDateTime) {
        this.idMovie = idMovie;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.isAdultMovie = isAdultMovie;
        this.isFavouriteMovie = isFavouriteMovie;
        this.overview = overview;
        this.reminderDateTime = reminderDateTime;
    }

    protected Movie(Parcel in) {
        idMovie = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        isAdultMovie = in.readByte() != 0;
        isFavouriteMovie = in.readByte() != 0;
        overview = in.readString();
        reminderDateTime = in.readString();
    }

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

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean isAdultMovie() {
        return isAdultMovie;
    }

    public void setAdultMovie(boolean adultMovie) {
        isAdultMovie = adultMovie;
    }

    public boolean isFavouriteMovie() {
        return isFavouriteMovie;
    }

    public void setFavouriteMovie(boolean favouriteMovie) {
        isFavouriteMovie = favouriteMovie;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReminderDateTime() {
        return reminderDateTime;
    }

    public void setReminderDateTime(String reminderDateTime) {
        this.reminderDateTime = reminderDateTime;
    }

    public int getTypeDisplay() {
        return typeDisplay;
    }

    public void setTypeDisplay(int typeDisplay) {
        this.typeDisplay = typeDisplay;
    }

    @Override
    public int compareTo(Object o) {
        Movie compare = (Movie) o;
        if (compare.idMovie == this.idMovie && compare.title.equals(this.title) && compare.overview.equals(this.overview)){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Movie clone;
        try {
            clone = (Movie) super.clone();
        }catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
        return clone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idMovie);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeByte((byte) (isAdultMovie ? 1 : 0));
        dest.writeByte((byte) (isFavouriteMovie ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(reminderDateTime);
    }

    public void readFromParcel(Parcel in) {
        idMovie = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isAdultMovie = in.readBoolean();
            isFavouriteMovie = in.readBoolean();
        }else{
            isAdultMovie = in.readByte() != 0;
            isFavouriteMovie = in.readByte() != 0;
        }
        overview = in.readString();
        reminderDateTime = in.readString();

    }
}

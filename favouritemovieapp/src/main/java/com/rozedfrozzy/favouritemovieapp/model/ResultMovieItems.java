package com.rozedfrozzy.favouritemovieapp.model;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static android.provider.BaseColumns._ID;
import static com.rozedfrozzy.favouritemovieapp.db.DatabaseContract.FavouriteColumns.BACKDROP;
import static com.rozedfrozzy.favouritemovieapp.db.DatabaseContract.FavouriteColumns.OVERVIEW;
import static com.rozedfrozzy.favouritemovieapp.db.DatabaseContract.FavouriteColumns.POSTER;
import static com.rozedfrozzy.favouritemovieapp.db.DatabaseContract.FavouriteColumns.RELEASE_DATE;
import static com.rozedfrozzy.favouritemovieapp.db.DatabaseContract.FavouriteColumns.TITLE;
import static com.rozedfrozzy.favouritemovieapp.db.DatabaseContract.getColumnInt;
import static com.rozedfrozzy.favouritemovieapp.db.DatabaseContract.getColumnString;

public class ResultMovieItems implements Serializable{
    private int id;
    private String posterPath;
    private String title;
    private String overview;
    private String releaseDate;
    private String backdropPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public ResultMovieItems() {
    }

    public ResultMovieItems(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.posterPath = getColumnString(cursor, POSTER);
        this.title = getColumnString(cursor, TITLE);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.releaseDate = getColumnString(cursor, RELEASE_DATE);
        this.backdropPath = getColumnString(cursor, BACKDROP);
    }
}

package com.example.mymoviememoir.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Watchlist {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "movie_name")
    public String movieName;

    @ColumnInfo(name = "release_date")
    public String releaseDate;

    @ColumnInfo(name = "added_date")
    public String addedDate;

    public Watchlist(int uid, String movieName, String releaseDate, String addedDate) {
        this.uid = uid;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.addedDate = addedDate;
    }

    public int getUid() {
        return uid;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }
}

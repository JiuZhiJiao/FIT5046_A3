package com.example.mymoviememoir.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymoviememoir.entity.Watchlist;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WatchlistDAO {

    @Query("SELECT * FROM watchlist")
    LiveData<List<Watchlist>> getAll();

    @Query("SELECT * FROM watchlist WHERE uid = :watchlistId LIMIT 1")
    Watchlist findByID(int watchlistId);

    @Query("SELECT * FROM watchlist WHERE movie_name = :name LIMIT 1")
    Watchlist findByName(String name);

    @Insert
    void insertAll(Watchlist... watchlists);

    @Insert
    long insert(Watchlist watchlist);

    @Delete
    void delete(Watchlist watchlist);

    @Update(onConflict =  REPLACE)
    void updateWatchlist(Watchlist... watchlists);

    @Query("DELETE FROM watchlist")
    void deleteAll();

}

package com.example.mymoviememoir.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymoviememoir.entity.Watchlist;
import com.example.mymoviememoir.repository.WatchlistRepository;

import java.util.List;

public class WatchlistViewModel extends ViewModel {

    private WatchlistRepository cRepository;
    private MutableLiveData<List<Watchlist>> allWatchlist;

    public WatchlistViewModel() {
        allWatchlist = new MutableLiveData<>();
    }

    public void setAllWatchlist(List<Watchlist> watchlist) {
        allWatchlist.setValue(watchlist);
    }

    public LiveData<List<Watchlist>> getAllWatchlist() {
        return cRepository.getAllWatchlist();
    }

    public void initializeVars(Application application) {
        cRepository = new WatchlistRepository(application);
    }

    public void insert(Watchlist watchlist) {
        cRepository.insert(watchlist);
    }

    public void insertAll(Watchlist... watchlists) {
        cRepository.insertAll(watchlists);
    }

    public void deleteAll() {
        cRepository.deleteAll();
    }

    public void delete(Watchlist watchlist) {
        cRepository.delete(watchlist);
    }

    public void update(Watchlist... watchlists) {
        cRepository.updateWatchlist(watchlists);
    }

    public Watchlist findByID(int watchlistId) {
        return cRepository.findByID(watchlistId);
    }

    public Watchlist findByName(String name) {
        return cRepository.findByName(name);
    }

}

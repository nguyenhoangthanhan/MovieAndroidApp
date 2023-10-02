package com.example.movieapp.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.movieapp.database.MoviesDao;
import com.example.movieapp.database.MoviesDatabase;
import com.example.movieapp.entity.Favourite;
import com.example.movieapp.model.Movie;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class FavouriteRepository {

    private final MoviesDao moviesDao;

    public FavouriteRepository(Application application){
        MoviesDatabase db = MoviesDatabase.getInstance(application);
        moviesDao = db.moviesDao();
    }

    public Flowable<List<Favourite>> getAllFavourite(Long idUser) {
        return moviesDao.getAllFavourite(idUser);
    }

    public Flowable<Favourite> getFavouriteById(int idMovie, Long idUser){
        return moviesDao.getFavouriteById(idMovie, idUser);
    }

    public void insertFavourite(Favourite favourite){
        MoviesDatabase.databaseWriteExecutor.execute(() -> {
            moviesDao.insert(favourite);
        });
    }
}

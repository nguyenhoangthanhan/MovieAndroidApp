package com.example.movieapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.movieapp.entity.Favourite;
import com.example.movieapp.utils.CONSTANT;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface MoviesDao {

    @Transaction
    @Query("SELECT * FROM " + CONSTANT.NAME_OF_FAVOURITE_TABLE + " WHERE " + CONSTANT.USER_ID + " = :userId")
    Flowable<List<Favourite>> getAllFavourite(Long userId);

    @Query("SELECT * FROM " + CONSTANT.NAME_OF_FAVOURITE_TABLE + " WHERE " + CONSTANT.MOVIE_ID + " = :idMovie AND "
            + CONSTANT.USER_ID + " = :userId")
    Flowable<Favourite> getFavouriteById(int idMovie, Long userId);

    @Query("DELETE FROM " + CONSTANT.NAME_OF_FAVOURITE_TABLE + " WHERE " + CONSTANT.MOVIE_ID + " = :idMovie AND "
            + CONSTANT.USER_ID + " = :userId")
    void unLikeMovie(int idMovie, Long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Favourite favourite);
}

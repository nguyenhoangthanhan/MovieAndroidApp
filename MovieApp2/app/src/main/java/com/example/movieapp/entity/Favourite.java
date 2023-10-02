package com.example.movieapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.movieapp.utils.CONSTANT;

@Entity(tableName = CONSTANT.NAME_OF_FAVOURITE_TABLE)
public class Favourite {

    @PrimaryKey
    @ColumnInfo(name = CONSTANT.MOVIE_ID)
    public int movieId;

    @ColumnInfo(name = CONSTANT.IS_FAVOURITE)
    public boolean isFavourite;

    @ColumnInfo(name = CONSTANT.USER_ID)
    public Long idUser;

    public Favourite(int movieId, boolean isFavourite, Long idUser) {
        this.movieId = movieId;
        this.isFavourite = isFavourite;
        this.idUser = idUser;
    }
}

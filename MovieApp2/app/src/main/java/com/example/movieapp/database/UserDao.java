package com.example.movieapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.movieapp.entity.User;
import com.example.movieapp.utils.CONSTANT;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface UserDao {

    @Transaction
    @Query("SELECT * FROM " + CONSTANT.NAME_OF_USER_TABLE)
    Flowable<List<User>> getAllUsers();

    @Query("SELECT * FROM " + CONSTANT.NAME_OF_USER_TABLE + " WHERE " + CONSTANT.USER_ID + " = :idUser")
    Flowable<User> getUserById(Long idUser);

    @Query("SELECT * FROM " + CONSTANT.NAME_OF_USER_TABLE + " LIMIT 1")
    Maybe<User> getFirstUser() ;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(User user) ;
}

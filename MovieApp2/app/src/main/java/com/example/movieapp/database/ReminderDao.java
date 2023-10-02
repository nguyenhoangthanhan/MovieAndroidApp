package com.example.movieapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.movieapp.entity.Reminder;
import com.example.movieapp.utils.CONSTANT;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ReminderDao {

    @Transaction
    @Query("SELECT * FROM " + CONSTANT.NAME_OF_REMINDER_TABLE + " WHERE " + CONSTANT.USER_ID + " = :userId"
            + " ORDER BY " + CONSTANT.MOVIE_ID)
    Flowable<List<Reminder>> getAllReminder(Long userId);

    @Query("SELECT * FROM " + CONSTANT.NAME_OF_REMINDER_TABLE + " WHERE " + CONSTANT.USER_ID + " = :userId"
            + " ORDER BY " + CONSTANT.MOVIE_ID + " LIMIT 2")
    Flowable<List<Reminder>> get2Reminder(Long userId);

    @Transaction
    @Query("SELECT DISTINCT "+ CONSTANT.MOVIE_ID+" FROM " + CONSTANT.NAME_OF_REMINDER_TABLE + " ORDER BY " + CONSTANT.MOVIE_ID)
    Flowable<List<Integer>> getAllListIdMovieDistinct();

    @Query("SELECT * FROM " + CONSTANT.NAME_OF_REMINDER_TABLE + " WHERE " + CONSTANT.MOVIE_ID + " = :idMovie AND "
    + CONSTANT.USER_ID + " = :userId")
    Flowable<Reminder> getReminderById(int idMovie, int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertReminder(Reminder reminder);

    @Query("UPDATE " + CONSTANT.NAME_OF_REMINDER_TABLE + " SET " + CONSTANT.REMINDER_TIME_COLUMN + " = :newDateTime")
    void updateReminder(String newDateTime);

    @Query("DELETE FROM " + CONSTANT.NAME_OF_REMINDER_TABLE + " WHERE " + CONSTANT.MOVIE_ID + " = :idMovie AND "
            + CONSTANT.USER_ID + " = :userId")
    Completable deleteReminderByIdMovie(int idMovie, Long userId);

}

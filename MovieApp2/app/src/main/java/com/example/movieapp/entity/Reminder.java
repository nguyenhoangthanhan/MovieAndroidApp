package com.example.movieapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.movieapp.utils.CONSTANT;

@Entity(tableName = CONSTANT.NAME_OF_REMINDER_TABLE)
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CONSTANT.MOVIE_ID)
    public int movieId;

    @ColumnInfo(name = CONSTANT.REMINDER_TIME_COLUMN)
    public String reminderTime;

    @ColumnInfo(name = CONSTANT.USER_ID)
    public Long idUser;

    public Reminder(int movieId, String reminderTime, Long idUser) {
        this.movieId = movieId;
        this.reminderTime = reminderTime;
        this.idUser = idUser;
    }
}

package com.example.movieapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.movieapp.entity.Favourite;
import com.example.movieapp.entity.Reminder;
import com.example.movieapp.entity.User;
import com.example.movieapp.utils.CONSTANT;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Favourite.class, Reminder.class, User.class}, version = 10)
public abstract class MoviesDatabase extends RoomDatabase {
    public abstract MoviesDao moviesDao();
    public abstract ReminderDao reminderDao();
    public abstract UserDao userDao();

    private static volatile MoviesDatabase moviesDatabase = null;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MoviesDatabase getInstance(final Context context) {
        if (moviesDatabase == null) {
            synchronized (MoviesDatabase.class){
                if (moviesDatabase == null) {
                    moviesDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    MoviesDatabase.class, CONSTANT.DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return moviesDatabase;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}

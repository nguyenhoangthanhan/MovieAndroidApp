package com.example.movieapp.repository;

import android.app.Application;
import com.example.movieapp.database.MoviesDatabase;
import com.example.movieapp.database.ReminderDao;
import com.example.movieapp.entity.Reminder;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class ReminderRepository {

    private final ReminderDao reminderDao;

    public ReminderRepository(Application application){
        MoviesDatabase db = MoviesDatabase.getInstance(application);
        reminderDao = db.reminderDao();
    }

    public Flowable<Reminder> getReminderById(int idMovie, int idUser){
        return reminderDao.getReminderById(idMovie, idUser);
    }

    public Flowable<List<Reminder>> getAllReminder(Long idUser){
        return reminderDao.getAllReminder(idUser);
    }

    public Flowable<List<Integer>> getAllListIdMovieDistinct(){
        return reminderDao.getAllListIdMovieDistinct();
    }

    public Flowable<List<Reminder>> get2Reminder(Long idUser){
        return reminderDao.get2Reminder(idUser);
    }

    public synchronized Completable insertReminder(Reminder reminder){
        return reminderDao.insertReminder(reminder);
    }

    public synchronized Completable deleteReminderByIdMovie(int idMovie, Long idUser){
        return reminderDao.deleteReminderByIdMovie(idMovie, idUser);
    }
}

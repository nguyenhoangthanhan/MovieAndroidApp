package com.example.movieapp.view_model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.entity.Reminder;
import com.example.movieapp.repository.ReminderRepository;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;

import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReminderViewModel extends AndroidViewModel {

    private final ReminderRepository reminderRepository;
    private final MutableLiveData<List<Reminder>> movieReminderList = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> idMovieDistinctList = new MutableLiveData<>();

    public ReminderViewModel(Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application);
    }

    public void deleteAllOldReminder(CompositeDisposable compositeDisposable, Long idUser){
        Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder=");
        compositeDisposable.add(reminderRepository.getAllReminder(idUser).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                reminders->{
                    for (int i = 0; i < reminders.size(); i++){
                        int year = Utils.getYearFromFormatFull(reminders.get(i).reminderTime);
                        int month = Utils.getMonthFromFormatFull(reminders.get(i).reminderTime);month--;
                        int day = Utils.getDayFromFormatFull(reminders.get(i).reminderTime);
                        int hour = Utils.getHourFromFormatFull(reminders.get(i).reminderTime);
                        int minute = Utils.getMinuteFromFormatFull(reminders.get(i).reminderTime);
                        int second = Utils.getSecondFromFormatFull(reminders.get(i).reminderTime);
                        Log.d(CONSTANT.LOG_NOTIFICATION, "--------------------------------------------------------");
                        Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#year="+year);
                        Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#month="+month);
                        Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#day="+day);
                        Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#hour="+hour);
                        Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#minute="+minute);
                        Calendar calendarCurrent = Calendar.getInstance();
                        Calendar calendarOld = Calendar.getInstance();
                        calendarOld.set(year, month, day, hour, minute, second);
                        if (calendarOld.get(Calendar.YEAR) < calendarCurrent.get(Calendar.YEAR)){
                            Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#delete.Year");
                            deleteReminderByIdMovie(reminders.get(i).movieId, idUser, compositeDisposable);
                        }
                        else if (calendarOld.get(Calendar.YEAR) == calendarCurrent.get(Calendar.YEAR)){
                            Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#delete.YEAR==YEAR");
                            Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#calendarOld.get(Calendar.MONTH)="
                                    +calendarOld.get(Calendar.MONTH));
                            Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#calendarCurrent.get(Calendar.MONTH)="
                                    +calendarCurrent.get(Calendar.MONTH));
                            if (calendarOld.get(Calendar.MONTH) < calendarCurrent.get(Calendar.MONTH)){
                                Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#delete.MONTH");
                                deleteReminderByIdMovie(reminders.get(i).movieId, idUser, compositeDisposable);
                            }
                            else if (calendarOld.get(Calendar.MONTH) == calendarCurrent.get(Calendar.MONTH)){
                                Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#delete.MONTH==MONTH");
                                if (calendarOld.get(Calendar.DAY_OF_MONTH) < calendarCurrent.get(Calendar.DAY_OF_MONTH)){
                                    Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#delete.DAY_OF_MONTH");
                                    deleteReminderByIdMovie(reminders.get(i).movieId, idUser, compositeDisposable);
                                }
                                else if (calendarOld.get(Calendar.DAY_OF_MONTH) == calendarCurrent.get(Calendar.DAY_OF_MONTH)){
                                    Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#delete.DAY_OF_MONTH==DAY_OF_MONTH");
                                    if (calendarOld.get(Calendar.HOUR_OF_DAY) < calendarCurrent.get(Calendar.HOUR_OF_DAY)){
                                        Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#delete.HOUR_OF_DAY");
                                        deleteReminderByIdMovie(reminders.get(i).movieId, idUser, compositeDisposable);
                                    }
                                    else if (calendarOld.get(Calendar.HOUR_OF_DAY) == calendarCurrent.get(Calendar.HOUR_OF_DAY)){
                                        Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#delete.HOUR_OF_DAY==HOUR_OF_DAY");
                                        if (minute < calendarCurrent.get(Calendar.MINUTE)){
                                            Log.d(CONSTANT.LOG_NOTIFICATION, "deleteAllOldReminder#delete.MINUTE");
                                            deleteReminderByIdMovie(reminders.get(i).movieId, idUser, compositeDisposable);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Log.d(CONSTANT.LOG_NOTIFICATION, "--------------------------------------------------------");

                }, ViewModelManager::handleError
        ));
    }

    public void getAllReminder(CompositeDisposable compositeDisposable, Long idUser){
        compositeDisposable.add(reminderRepository.getAllReminder(idUser).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                movieReminderList::setValue, ViewModelManager::handleError
        ));
    }

    public Flowable<List<Reminder>> getAllReminderRx(Long idUser){
        return reminderRepository.getAllReminder(idUser);
    }

    public void getAllListIdMovieDistinct(CompositeDisposable compositeDisposable){
        compositeDisposable.add(reminderRepository.getAllListIdMovieDistinct().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                idMovieDistinctList::setValue, ViewModelManager::handleError
        ));
    }

    public Flowable<List<Reminder>> get2Reminder(Long idUser){
        return reminderRepository.get2Reminder(idUser);
    }

    public Completable insertReminder(Reminder reminder){
        Log.d(CONSTANT.LOG_NOTIFICATION, "insertReminder#idMovie="+reminder.movieId);
        return reminderRepository.insertReminder(reminder);
    }

    public MutableLiveData<List<Reminder>> getMovieReminderList() {
        return movieReminderList;
    }

    public void deleteReminderByIdMovie(int idMovie, Long idUser, CompositeDisposable compositeDisposable){
        Log.d(CONSTANT.LOG_NOTIFICATION, "deleteReminderByIdMovie#idMovie="+idMovie);
        compositeDisposable.add(reminderRepository.deleteReminderByIdMovie(idMovie, idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(()->{}, ViewModelManager::handleError));
    }
}

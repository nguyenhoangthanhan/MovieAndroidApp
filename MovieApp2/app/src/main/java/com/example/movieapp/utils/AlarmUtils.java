package com.example.movieapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.movieapp.model.Movie;
import com.example.movieapp.receiver.MovieNotificationReceiver;

import java.text.ParseException;
import java.util.Calendar;

public class AlarmUtils {

    public static void create(Context context, Movie movie, int newRequestCode) {
        Log.d(CONSTANT.LOG_NOTIFICATION, "create#start");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent broadcast_intent = new Intent(context, MovieNotificationReceiver.class);
        String contentText = "Year: " + Utils.getYear(movie.getReleaseDate()) + "\n" + "Rate: " + movie.getVoteAverage() + "/10";
        broadcast_intent.putExtra(CONSTANT.KEY_TITLE_TEXT, movie.getTitle());
        broadcast_intent.putExtra(CONSTANT.KEY_CONTENT_TEXT, contentText);
        broadcast_intent.putExtra(CONSTANT.KEY_SEND_ID_MOVIE_NOTIFICATION, movie.getIdMovie());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, newRequestCode,  broadcast_intent, 0);

        Calendar calendar = Calendar.getInstance();
        try {
            String timePush = movie.getReminderDateTime();
            calendar.setTime(Utils.getFormatFull().parse(timePush));
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } catch (ParseException e) {
            Log.e(CONSTANT.LOG_NOTIFICATION, e.getMessage());
        }

        Log.d(CONSTANT.LOG_NOTIFICATION, "create#end");
    }

}

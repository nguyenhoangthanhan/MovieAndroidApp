package com.example.movieapp.receiver;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.movieapp.MovieApplication;
import com.example.movieapp.R;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;

import java.util.Calendar;

public class MovieNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(CONSTANT.LOG_NOTIFICATION, "onReceive#start");

        String title = intent.getStringExtra(CONSTANT.KEY_TITLE_TEXT);
        String contentText = intent.getStringExtra(CONSTANT.KEY_CONTENT_TEXT);
        int idMovie = intent.getIntExtra(CONSTANT.KEY_SEND_ID_MOVIE_NOTIFICATION, -1);

        if (idMovie != -1){
            createNewNotification(title, context, contentText, idMovie);
        }
    }

    private void createNewNotification(String title, Context context, String contentText, int idMovie) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.beginner3);
        Notification notification;

        notification = new NotificationCompat.Builder(context, MovieApplication.MOVIE_APP_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.beginner3)
                .setLargeIcon(largeIcon)
                .setColor(Color.BLACK).build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(idMovie, notification);
        deleteNotificationInDB(context, idMovie);
    }

    private void deleteNotificationInDB(Context context, int idMovie) {

    }
}

package com.example.movieapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.LocaleHelper;

public class MovieApplication extends Application {

    public static final String MOVIE_APP_CHANNEL_ID = "ID_CHANNEL_APP";
    private static final String MOVIE_APP_CHANNEL_NAME = "CHANNEL MOVIE APP";
    private static final String MOVIE_APP_DESCRIPTION_MOVIE_APP = "This is a channel used to push notification in movie app";

    public static Long idUserCurrent = -1L;

    @Override
    public void onCreate() {
        super.onCreate();

        MovieApplication.idUserCurrent = SharedPreferencesManager.getLongValue(getApplicationContext(), SharedPreferencesManager.KEY_ID_USER_CURRENT, -1L);
        Log.d(CONSTANT.TAG_MOVIE_APPLICATION, "#onCreate.idUserCurrent = " + MovieApplication.idUserCurrent);


        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = MOVIE_APP_CHANNEL_NAME;
            String description = MOVIE_APP_DESCRIPTION_MOVIE_APP;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(MOVIE_APP_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

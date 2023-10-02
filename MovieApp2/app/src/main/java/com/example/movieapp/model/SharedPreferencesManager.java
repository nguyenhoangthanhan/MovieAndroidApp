package com.example.movieapp.model;

import android.content.Context;
import android.content.LocusId;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    public static final String MOVIES_SHARE_PREFERENCES = "MOVIES_SHARE_PREFERENCES";

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_DAY_OF_BIRTH = "day_of_birth";
    public static final String KEY_SEX = "sex";
    public static final String KEY_IMG_AVATAR = "image_avatar";

    public static final String KEY_MOVIE_CATEGORY = "MOVIE_CATEGORY";
    public static final String KEY_MOVIE_WITH_RATE_FROM = "MOVIE_WITH_RATE_FROM";
    public static final String KEY_YEAR_FROM = "YEAR_FROM";
    public static final String KEY_SORT_BY = "SORT_BY";

    public static final String KEY_ID_NOTIFICATION = "id_notification";
    public static final String KEY_ID_USER_CURRENT = "id_user_current";

    public static final String KEY_LANGUAGE = "KEY_LANGUAGE";

    public static final String JAPANESE = "Japanese language";
    public static final String ENGLISH = "English language";


    private SharedPreferencesManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(MOVIES_SHARE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static String getStringValue(Context context, String key, String defaultValue) {
        return getSharedPreferences(context).getString(key , defaultValue);
    }

    public static int getIntValue(Context context, String key, int defaultValue) {
        return getSharedPreferences(context).getInt(key , defaultValue);
    }

    public static Long getLongValue(Context context, String key, Long defaultValue) {
        return getSharedPreferences(context).getLong(key , defaultValue);
    }

    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(key , defaultValue);
    }

    public static void setStringValue(Context context, String newValue, String key) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key , newValue);
        editor.apply();
    }

    public static void setIntValue(Context context, int newValue, String key) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key , newValue);
        editor.apply();
    }

    public static void setLongValue(Context context, Long newValue, String key) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(key , newValue);
        editor.apply();
    }

    public static void setBooleanValue(Context context, boolean newValue, String key) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key , newValue);
        editor.apply();
    }
}

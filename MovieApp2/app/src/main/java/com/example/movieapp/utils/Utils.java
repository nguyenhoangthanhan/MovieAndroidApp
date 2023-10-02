package com.example.movieapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kotlin.jvm.JvmStatic;

public class Utils {

    private static final String TAG_UTILS = "Utils";

    public static boolean isValidEmail(String email) {
        email = email.trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        return email.matches(emailPattern);
    }

    @JvmStatic
    @BindingAdapter("setLoadImage")
    public static void setLoadImage(Context context, ImageView image, String url) {
        if (!url.isEmpty()) {
            Glide.with(context).load(url).placeholder(R.drawable.beginner3).dontAnimate().into(image);
        }
    }

    public static int getYear(String dateTime){
        if (dateTime.equals("")) return 1;
        String[] parts = dateTime.split("-");
        return Integer.parseInt(parts[0]);
    }

    public static int getMonth(String dateTime){
        if (dateTime.equals("")) return 1;
        String[] parts = dateTime.split("-");
        return Integer.parseInt(parts[1]);
    }

    public static int getDay(String dateTime){
        if (dateTime.equals("")) return 1;
        String[] parts = dateTime.split("-");
        return Integer.parseInt(parts[2]);
    }

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getFormatFull(){
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getFormatReleaseDate(){
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static int getYearFromFormatFull(String dateTime){
        if (dateTime.equals("")) return 1;
        String[] parts = dateTime.split(" ");
        String[] parts2 = parts[0].split("/");
        return Integer.parseInt(parts2[0]);
    }

    public static int getMonthFromFormatFull(String dateTime){
        if (dateTime.equals("")) return 1;
        String[] parts = dateTime.split(" ");
        String[] parts2 = parts[0].split("/");
        return Integer.parseInt(parts2[1]);
    }

    public static int getDayFromFormatFull(String dateTime){
        if (dateTime.equals("")) return 1;
        String[] parts = dateTime.split(" ");
        String[] parts2 = parts[0].split("/");
        return Integer.parseInt(parts2[2]);
    }

    public static int getHourFromFormatFull(String dateTime){
        if (dateTime.equals("")) return 1;
        String[] parts = dateTime.split(" ");
        String[] parts2 = parts[1].split(":");
        return Integer.parseInt(parts2[0]);
    }

    public static int getMinuteFromFormatFull(String dateTime){
        if (dateTime.equals("")) return 1;
        String[] parts = dateTime.split(" ");
        String[] parts2 = parts[1].split(":");
        return Integer.parseInt(parts2[1]);
    }

    public static int getSecondFromFormatFull(String dateTime){
        if (dateTime.equals("")) return 1;
        String[] parts = dateTime.split(" ");
        String[] parts2 = parts[1].split(":");
        return Integer.parseInt(parts2[2]);
    }



    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.e(CONSTANT.TAG_REMINDER_FRAGMENT, "sendNotification#eror - get img: " + e.getMessage());
            return null;
        }
    }

    public static void setLanguage(Context context) {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "LocaleHelper.getLanguage(requireContext()) = "+LocaleHelper.getLanguage(context));
        LocaleHelper.setLocale(context,LocaleHelper.getLanguage(context));
    }

}


package com.example.movieapp.view_model;

import android.util.Log;

import com.example.movieapp.utils.CONSTANT;

public class ViewModelManager {

    public static void handleError(Throwable error) {
        Log.e(CONSTANT.TAG_MOVIE_FRAGMENT, "handleError: " + error.getMessage());
    }

    public static void handleSuccess(String mess) {
        Log.i(CONSTANT.TAG_MOVIE_FRAGMENT, "handleSuccess: " + mess);
    }

    public static void handleSuccess() {
        Log.i(CONSTANT.TAG_MOVIE_FRAGMENT, "handleSuccess: " + "success!");
    }
}

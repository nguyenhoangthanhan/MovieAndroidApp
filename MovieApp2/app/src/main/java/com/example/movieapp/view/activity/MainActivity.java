package com.example.movieapp.view.activity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.movieapp.MovieApplication;
import com.example.movieapp.databinding.ActivityMainBinding;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.LocaleHelper;
import com.example.movieapp.utils.Utils;
import com.example.movieapp.view_model.MovieViewModel;

public class MainActivity extends AppCompatActivity{
    View view;
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        view = activityMainBinding.getRoot();
        setContentView(view);

        Utils.setLanguage(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
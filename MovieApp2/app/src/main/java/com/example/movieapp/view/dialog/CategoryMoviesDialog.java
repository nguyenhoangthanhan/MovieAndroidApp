package com.example.movieapp.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.movieapp.R;
import com.example.movieapp.databinding.DialogCategoryListBinding;
import com.example.movieapp.interfa.SettingDialogSelectedListener;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;

public class CategoryMoviesDialog extends DialogFragment {

    private DialogCategoryListBinding dialogCategoryListBinding;

    private SettingDialogSelectedListener settingDialogSelectedListener;

    public void setSettingDialogSelectedListener(SettingDialogSelectedListener settingDialogSelectedListener) {
        this.settingDialogSelectedListener = settingDialogSelectedListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogCategoryListBinding = DialogCategoryListBinding.inflate(inflater, container, false);
        return dialogCategoryListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        initEvent();
    }

    private void initView() {
        String currentSelectedCategory = SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_MOVIE_CATEGORY, getString(R.string.popular_movies));
        if (currentSelectedCategory.equals(getString(R.string.popular_movies))){
            dialogCategoryListBinding.radioGroupSelectFilmGenre.check(R.id.rb_popular_movies);
        }
        else if (currentSelectedCategory.equals(getString(R.string.top_rated_movies))){
            dialogCategoryListBinding.radioGroupSelectFilmGenre.check(R.id.rb_top_rated_movies);
        }
        else if (currentSelectedCategory.equals(getString(R.string.upcoming_movies))){
            dialogCategoryListBinding.radioGroupSelectFilmGenre.check(R.id.rb_upcoming_movies);
        }
        else if (currentSelectedCategory.equals(getString(R.string.now_playing_movies))){
            dialogCategoryListBinding.radioGroupSelectFilmGenre.check(R.id.rb_now_playing_movies);
        }
        else{
            dialogCategoryListBinding.radioGroupSelectFilmGenre.check(R.id.rb_popular_movies);
        }
    }

    private void initEvent() {
        dialogCategoryListBinding.btnCancel.setOnClickListener(v -> this.dismiss());

        dialogCategoryListBinding.rbPopularMovies.setOnClickListener(v -> {
            settingDialogSelectedListener.radioButtonSelectedListener(getString(R.string.popular_movies), CONSTANT.FLAG_CATEGORY);
            dialogCategoryListBinding.radioGroupSelectFilmGenre.check(R.id.rb_popular_movies);
            this.dismiss();
        });

        dialogCategoryListBinding.rbTopRatedMovies.setOnClickListener(v -> {
            settingDialogSelectedListener.radioButtonSelectedListener(getString(R.string.top_rated_movies), CONSTANT.FLAG_CATEGORY);
            dialogCategoryListBinding.radioGroupSelectFilmGenre.check(R.id.rb_top_rated_movies);
            this.dismiss();
        });

        dialogCategoryListBinding.rbUpcomingMovies.setOnClickListener(v -> {
            settingDialogSelectedListener.radioButtonSelectedListener(getString(R.string.upcoming_movies), CONSTANT.FLAG_CATEGORY);
            dialogCategoryListBinding.radioGroupSelectFilmGenre.check(R.id.rb_upcoming_movies);
            this.dismiss();
        });

        dialogCategoryListBinding.rbNowPlayingMovies.setOnClickListener(v -> {
            settingDialogSelectedListener.radioButtonSelectedListener(getString(R.string.now_playing_movies), CONSTANT.FLAG_CATEGORY);
            dialogCategoryListBinding.radioGroupSelectFilmGenre.check(R.id.rb_now_playing_movies);
            this.dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialogCategoryListBinding = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}

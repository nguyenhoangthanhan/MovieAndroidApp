package com.example.movieapp.view.fragment.setting_reminder;
import static com.example.movieapp.utils.CONSTANT.TAG_EDIT_PROFILE;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.movieapp.R;
import com.example.movieapp.databinding.FragmentSettingsBinding;
import com.example.movieapp.interfa.SettingDialogSelectedListener;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.LocaleHelper;
import com.example.movieapp.utils.Utils;
import com.example.movieapp.view.activity.MainActivity;
import com.example.movieapp.view.dialog.CategoryMoviesDialog;
import com.example.movieapp.view.dialog.SetRateDialog;
import com.example.movieapp.view.dialog.SortByDialog;
import com.example.movieapp.view.dialog.YearReleaseFromDialog;

import java.util.ArrayList;
import java.util.Locale;

public class SettingsFragment extends Fragment implements View.OnClickListener, SettingDialogSelectedListener {

    private static final String TAG = CONSTANT.TAG_SETTINGS_FRAGMENT;

    private FragmentSettingsBinding fragmentSettingsBinding;

    private final ReminderFragment reminderFragment = new ReminderFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView#start");
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG_EDIT_PROFILE, "onCreateView#start");
        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        return fragmentSettingsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG_EDIT_PROFILE, "onCreateView#start");
        super.onViewCreated(view, savedInstanceState);

        initData();

        initView();

        initEvent();
    }

    private void initData() {
    }

    private void initView() {
        fragmentSettingsBinding.tvCategory.setText(SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_MOVIE_CATEGORY, getString(R.string.popular_movies)));
        fragmentSettingsBinding.tvRating.setText(SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_MOVIE_WITH_RATE_FROM, "0"));
        fragmentSettingsBinding.tvYearRelease.setText(SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_YEAR_FROM, getString(R.string.year_demo)));
        fragmentSettingsBinding.tvSortBy.setText(SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_SORT_BY, getString(R.string.release_date)));
        if (LocaleHelper.getLanguage(requireContext()).equals("en")){
            fragmentSettingsBinding.rgSetLanguage.check(R.id.rb_english);
        }
        else{
            fragmentSettingsBinding.rgSetLanguage.check(R.id.rb_japanese);
        }
    }

    private void initEvent() {
        fragmentSettingsBinding.tvCategoryTitle.setOnClickListener(this);
        fragmentSettingsBinding.tvCategory.setOnClickListener(this);
        fragmentSettingsBinding.tvSortByTitle.setOnClickListener(this);
        fragmentSettingsBinding.tvSortBy.setOnClickListener(this);
        fragmentSettingsBinding.tvRatingTitle.setOnClickListener(this);
        fragmentSettingsBinding.tvRating.setOnClickListener(this);
        fragmentSettingsBinding.tvYearReleaseTitle.setOnClickListener(this);
        fragmentSettingsBinding.tvYearRelease.setOnClickListener(this);
        fragmentSettingsBinding.rbEnglish.setOnClickListener(this);
        fragmentSettingsBinding.rbJapanese.setOnClickListener(this);
    }

    public void openReminderFragment(){
        Log.d(TAG, "openReminderFragment#start");
        FragmentManager fragmentManager = getParentFragmentManager();
        if (fragmentManager.findFragmentByTag(CONSTANT.REMINDER_FROM_SETTINGS_FRAGMENT_TAG) == null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_settings_reminder, reminderFragment, CONSTANT.REMINDER_FROM_SETTINGS_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(CONSTANT.START_FROM_SETTINGS_FRAGMENT);
            fragmentTransaction.commit();
        }
        Log.d(TAG, "openReminderFragment#end");
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart#start");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume#start");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause#start");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop#start");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy#start");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach#start");
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView#start");
        super.onDestroyView();
        fragmentSettingsBinding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_category_title || v.getId() == R.id.tv_category){
            showDialogFilmCategory();
        }
        if (v.getId() == R.id.tv_sort_by_title || v.getId() == R.id.tv_sort_by){
            showDialogSortBy();
        }
        if (v.getId() == R.id.tv_rating || v.getId() == R.id.tv_rating_title){
            showDialogSetRate();
        }
        if (v.getId() == R.id.tv_year_from_title || v.getId() == R.id.tv_year_release){
            showDialogSetYearFrom();
        }
        if (v.getId() == R.id.rb_english){
            if (LocaleHelper.getLanguage(requireContext()).equals("jp")){
                LocaleHelper.setLocale(requireContext(),"en");
                reOpenActivity();
            }
        }
        if (v.getId() == R.id.rb_japanese){
            if (LocaleHelper.getLanguage(requireContext()).equals("en")){
                LocaleHelper.setLocale(requireContext(),"jp");
                reOpenActivity();
            }
        }
    }

    public void reOpenActivity() {
        Intent refresh = new Intent(requireContext(), MainActivity.class);
        requireActivity().finish();
        startActivity(refresh);
    }

    private void showDialogFilmCategory() {
        CategoryMoviesDialog categoryMoviesDialog = new CategoryMoviesDialog();
        categoryMoviesDialog.setSettingDialogSelectedListener(this);
        categoryMoviesDialog.show(getChildFragmentManager(), CONSTANT.TAG_DIALOG_FILM_CATEGORY);
    }

    private void showDialogSortBy() {
        SortByDialog sortByDialog = new SortByDialog();
        sortByDialog.setSettingDialogSelectedListener(this);
        sortByDialog.show(getChildFragmentManager(), CONSTANT.TAG_DIALOG_SORT_BY);
    }

    private void showDialogSetRate() {
        SetRateDialog setRateDialog = new SetRateDialog();
        setRateDialog.setSettingDialogSelectedListener(this);
        setRateDialog.show(getChildFragmentManager(), CONSTANT.TAG_DIALOG_SET_RATE);
    }

    private void showDialogSetYearFrom() {
        YearReleaseFromDialog yearReleaseFromDialog = new YearReleaseFromDialog();
        yearReleaseFromDialog.setSettingDialogSelectedListener(this);
        yearReleaseFromDialog.show(getChildFragmentManager(), CONSTANT.TAG_DIALOG_SET_YEAR_FROM);
    }

    @Override
    public void radioButtonSelectedListener(String selectedContent, String flag) {
        if (flag.equals(CONSTANT.FLAG_CATEGORY)){
            fragmentSettingsBinding.tvCategory.setText(selectedContent);
            SharedPreferencesManager.setStringValue(requireContext(), selectedContent, SharedPreferencesManager.KEY_MOVIE_CATEGORY);
        }
        if (flag.equals(CONSTANT.FLAG_SORT_BY)){
            fragmentSettingsBinding.tvSortBy.setText(selectedContent);
            SharedPreferencesManager.setStringValue(requireContext(), selectedContent, SharedPreferencesManager.KEY_SORT_BY);
        }
    }

    @Override
    public void rateSelectedListener(String rate) {
        fragmentSettingsBinding.tvRating.setText(rate);
        SharedPreferencesManager.setStringValue(requireContext(), rate, SharedPreferencesManager.KEY_MOVIE_WITH_RATE_FROM);
    }

    @Override
    public void yearFromSelectedListener(String year) {
        fragmentSettingsBinding.tvYearRelease.setText(year);
        SharedPreferencesManager.setStringValue(requireContext(), year, SharedPreferencesManager.KEY_YEAR_FROM);
    }
}

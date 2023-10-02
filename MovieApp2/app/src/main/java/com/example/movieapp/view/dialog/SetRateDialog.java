package com.example.movieapp.view.dialog;


import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.movieapp.R;
import com.example.movieapp.databinding.DialogSetMovieRateBinding;
import com.example.movieapp.interfa.SettingDialogSelectedListener;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;

public class SetRateDialog extends DialogFragment {

    private DialogSetMovieRateBinding dialogSetMovieRateBinding;

    private SettingDialogSelectedListener settingDialogSelectedListener;

    private String currentRate;

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
        dialogSetMovieRateBinding = DialogSetMovieRateBinding.inflate(inflater, container, false);
        return dialogSetMovieRateBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        initEvent();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogSetMovieRateBinding.progressBar.setMin(0);
        }
        dialogSetMovieRateBinding.progressBar.setMax(10);

        currentRate = SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_MOVIE_WITH_RATE_FROM, getString(R.string.rate_demo));
        dialogSetMovieRateBinding.progressBar.setProgress(Integer.parseInt(currentRate));
        dialogSetMovieRateBinding.tvSettingRate.setText(currentRate);
    }

    private void initEvent() {

        dialogSetMovieRateBinding.btnNo.setOnClickListener(v -> this.dismiss());

        dialogSetMovieRateBinding.btnYes.setOnClickListener(v -> {
            settingDialogSelectedListener.rateSelectedListener(currentRate);
            this.dismiss();
        });

        dialogSetMovieRateBinding.progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = 0;
                if(progress < min) {
                    seekBar.setProgress(min);
                }
                currentRate = progress + "";
                dialogSetMovieRateBinding.tvSettingRate.setText(currentRate);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialogSetMovieRateBinding = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}

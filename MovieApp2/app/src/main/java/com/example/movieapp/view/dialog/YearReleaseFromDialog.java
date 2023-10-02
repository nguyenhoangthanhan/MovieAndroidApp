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
import com.example.movieapp.databinding.DialogSetYearFromBinding;
import com.example.movieapp.interfa.SettingDialogSelectedListener;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;

public class YearReleaseFromDialog extends DialogFragment {

    private DialogSetYearFromBinding dialogSetYearFromBinding;

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
        dialogSetYearFromBinding = DialogSetYearFromBinding.inflate(inflater, container, false);
        return dialogSetYearFromBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        initEvent();
    }

    private void initView() {
        String currentYearFrom = (SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_YEAR_FROM, getString(R.string.year_demo)));
        dialogSetYearFromBinding.edtYearFrom.setText(currentYearFrom);
    }

    private void initEvent() {
        dialogSetYearFromBinding.btnNo.setOnClickListener(v -> this.dismiss());

        dialogSetYearFromBinding.btnYes.setOnClickListener(v -> {
            int yearFrom = Integer.parseInt(dialogSetYearFromBinding.edtYearFrom.getText().toString());
            if (yearFrom > CONSTANT.CURRENT_YEAR){
                dialogSetYearFromBinding.edtYearFrom.setError(getString(R.string.message_year_error));
            }
            else{
                settingDialogSelectedListener.yearFromSelectedListener(dialogSetYearFromBinding.edtYearFrom.getText().toString());
                this.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialogSetYearFromBinding = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}

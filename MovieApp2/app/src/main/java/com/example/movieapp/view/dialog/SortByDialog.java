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
import com.example.movieapp.databinding.DialogSortByBinding;
import com.example.movieapp.interfa.SettingDialogSelectedListener;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;

public class SortByDialog extends DialogFragment {

    private DialogSortByBinding dialogSortByBinding;

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
        dialogSortByBinding = DialogSortByBinding.inflate(inflater, container, false);
        return dialogSortByBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        initEvent();
    }

    private void initView() {
        String currentSelectedSortBy = SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_SORT_BY, getString(R.string.release_date_normal));
        if (currentSelectedSortBy.equals(getString(R.string.release_date_normal))){
            dialogSortByBinding.rgSortBy.check(R.id.rb_release_date);
        }
        else if (currentSelectedSortBy.equals(getString(R.string.rating_normal))){
            dialogSortByBinding.rgSortBy.check(R.id.rb_rating);
        }
        else{
            dialogSortByBinding.rgSortBy.check(R.id.rb_release_date);
        }
    }

    private void initEvent() {
        dialogSortByBinding.btnCancel.setOnClickListener(v -> this.dismiss());

        dialogSortByBinding.rbReleaseDate.setOnClickListener(v -> {
            settingDialogSelectedListener.radioButtonSelectedListener(getString(R.string.release_date_normal), CONSTANT.FLAG_SORT_BY);
            dialogSortByBinding.rgSortBy.check(R.id.rb_release_date);
            this.dismiss();
        });

        dialogSortByBinding.rbRating.setOnClickListener(v -> {
            settingDialogSelectedListener.radioButtonSelectedListener(getString(R.string.rating_normal), CONSTANT.FLAG_SORT_BY);
            dialogSortByBinding.rgSortBy.check(R.id.rb_rating);
            this.dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialogSortByBinding = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}

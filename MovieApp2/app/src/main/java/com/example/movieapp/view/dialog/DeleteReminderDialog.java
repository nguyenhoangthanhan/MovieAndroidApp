package com.example.movieapp.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.movieapp.databinding.DialogDeleteReminderBinding;
import com.example.movieapp.utils.Utils;

public class DeleteReminderDialog extends DialogFragment {

    private DialogDeleteReminderBinding dialogDeleteReminderBinding;

    private DeleteReminderClickListener deleteReminderClickListener;

    private String currentRate;

    public void setDeleteReminderClickListener(DeleteReminderClickListener deleteReminderClickListener) {
        this.deleteReminderClickListener = deleteReminderClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogDeleteReminderBinding = DialogDeleteReminderBinding.inflate(inflater, container, false);
        return dialogDeleteReminderBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initEvent();
    }

    private void initEvent() {

        dialogDeleteReminderBinding.tvCancel.setOnClickListener(v -> this.dismiss());

        dialogDeleteReminderBinding.tvDelete.setOnClickListener(v -> {
            deleteReminderClickListener.deleteReminderDialog();
            this.dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialogDeleteReminderBinding = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public interface DeleteReminderClickListener{
        void deleteReminderDialog();
    }
}

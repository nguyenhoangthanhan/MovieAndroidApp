package com.example.movieapp.view.fragment.user_info;
import static com.example.movieapp.utils.CONSTANT.TAG_EDIT_PROFILE;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.movieapp.MovieApplication;
import com.example.movieapp.R;
import com.example.movieapp.databinding.FragmentEditProfileBinding;
import com.example.movieapp.entity.User;
import com.example.movieapp.interfa.UpdateUserInfoListener;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;
import com.example.movieapp.view_model.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private UserViewModel userViewModel;

    private FragmentEditProfileBinding fragmentEditProfileBinding;

    private String encodedImage = "";

    private UpdateUserInfoListener updateUserInfoListener;

    private User  mUser;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void setUpdateUserInfoListener(UpdateUserInfoListener updateUserInfoListener) {
        this.updateUserInfoListener = updateUserInfoListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG_EDIT_PROFILE, "onCreateView#start");
        fragmentEditProfileBinding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return fragmentEditProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG_EDIT_PROFILE, "onViewCreated#start");
        super.onViewCreated(view, savedInstanceState);


        init();
        setOnClick();
        Log.d(TAG_EDIT_PROFILE, "onViewCreated#end");
    }

    private void init() {
        Log.d(TAG_EDIT_PROFILE, "#init");
        Log.d(TAG_EDIT_PROFILE, "#init.MovieApplication.idUserCurrent = " + MovieApplication.idUserCurrent);
        compositeDisposable.add(userViewModel.getUserById(MovieApplication.idUserCurrent).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                user -> {
                    if (user == null){
                        Log.d(TAG_EDIT_PROFILE, "#init.user = null");
                    }
                    else{
                        Log.d(TAG_EDIT_PROFILE, "#init.user = " + user.toString());
                        mUser = user;
                        if (fragmentEditProfileBinding != null){
                            Log.d(TAG_EDIT_PROFILE, "#init.fragmentEditProfileBinding is not null");
                            fragmentEditProfileBinding.edtUserName.setText(user.name);
                            fragmentEditProfileBinding.edtUserEmail.setText(user.email);
                            fragmentEditProfileBinding.dayOfBirth.setText(user.birthday);

                            if (user.sex.equals(CONSTANT.SEX_MALE)) fragmentEditProfileBinding.radioGroupSelectSex.check(R.id.radio_male);
                            else fragmentEditProfileBinding.radioGroupSelectSex.check(R.id.radio_female);

                            String imgAvatar = user.avatar;

                            if (!imgAvatar.equals("")){
                                try {
                                    byte[] b = Base64.decode(imgAvatar, Base64.DEFAULT);
                                    Log.d(TAG_EDIT_PROFILE, "getAvatar#b.length" + b.length);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                                    fragmentEditProfileBinding.imgAvatarUser.setImageBitmap(bitmap);
                                } catch (IllegalArgumentException e) {
                                    // java.lang.IllegalArgumentException: bad base-64
                                    android.util.Log.i(TAG_EDIT_PROFILE, "", e);
                                }
                            }
                        }
                        else {
                            Log.d(TAG_EDIT_PROFILE, "#init.fragmentEditProfileBinding = null");
                        }
                    }
                }
        ));

    }

    private void setOnClick() {
        fragmentEditProfileBinding.btnDone.setOnClickListener(this);
        fragmentEditProfileBinding.btnCancel.setOnClickListener(this);
        fragmentEditProfileBinding.dayOfBirth.setOnClickListener(this);
        fragmentEditProfileBinding.imgAvatarUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_done){
            String email = fragmentEditProfileBinding.edtUserEmail.getText().toString();
            if (Utils.isValidEmail(email)){
                saveDataOfUserToDB();
            }else{
                fragmentEditProfileBinding.edtUserEmail.setError(getString(R.string.email_error_message));
            }
        }
        if (v.getId() == R.id.btn_cancel){
            requireActivity().onBackPressed();
        }
        if (v.getId() == R.id.day_of_birth){
            setDayOfBirth();
        }
        if (v.getId() == R.id.img_avatar_user){
            if (ContextCompat.checkSelfPermission(requireContext()
                    , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGalleryToGetPhoto();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void saveDataOfUserToDB() {

        if (mUser != null && fragmentEditProfileBinding != null){
            mUser.birthday = fragmentEditProfileBinding.dayOfBirth.getText().toString();
            if (fragmentEditProfileBinding.radioMale.isChecked()){
                mUser.sex = CONSTANT.SEX_MALE;
            }
            else{
                mUser.sex = CONSTANT.SEX_FEMALE;
            }
            mUser.name = fragmentEditProfileBinding.edtUserName.getText().toString();
            mUser.email = fragmentEditProfileBinding.edtUserEmail.getText().toString();
            mUser.avatar = encodedImage;

            Log.d(TAG_EDIT_PROFILE, "#saveDataOfUserToDB.mUser = " + mUser.toString());
            compositeDisposable.add(userViewModel.insert(mUser)
                    .subscribeOn(Schedulers.newThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            ()->{
                                Log.d(CONSTANT.TAG_USER_VIEW_MODEL, "Edited user success!");
                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(requireContext(),
                                            "Edited user success!",Toast.LENGTH_SHORT).show();
                                    backToMainScreen();
                                });
                            },
                            error -> {
                                Log.d(CONSTANT.TAG_USER_VIEW_MODEL, "Edited user fail!");
                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(requireContext(),
                                            "Edited user fail!",Toast.LENGTH_SHORT).show();
                                    backToMainScreen();
                                });
                            }
                    ));
        }
    }

    private void backToMainScreen() {
        requireActivity().onBackPressed();
        updateUserInfoListener.updateUserInfo();
    }

    private void setDayOfBirth() {
        Log.d(TAG_EDIT_PROFILE, "setDayOfBirth#start");
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog= new DatePickerDialog(getActivity(), datePickerListener, mYear, mMonth, mDay);
        dateDialog.show();
        Log.d(TAG_EDIT_PROFILE, "setDayOfBirth#end");
    }

    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
            fragmentEditProfileBinding.dayOfBirth.setText(date);
        }
    };

    private void openGalleryToGetPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startForResultFromGallery.launch(intent);
    }

    private final ActivityResultLauncher<Intent> startForResultFromGallery =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK){
                try {
                    if (result.getData() != null){
                        Uri selectedImageUri = result.getData().getData();
                        Bitmap bitmap = BitmapFactory.decodeStream(requireContext().getContentResolver().openInputStream(selectedImageUri));

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();

                        encodedImage = android.util.Base64.encodeToString(b, Base64.DEFAULT);

                        // set bitmap to image view here........
                        fragmentEditProfileBinding.imgAvatarUser.setImageBitmap(bitmap);
                    }
                }catch (Exception exception){
                    Log.d("TAG",""+exception.getLocalizedMessage());
                }
            }
        }
    });

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    openGalleryToGetPhoto();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(requireContext(), getString(R.string.message_open_gallery), Toast.LENGTH_LONG).show();
                }
            });

    @Override
    public void onStart() {
        Log.d(TAG_EDIT_PROFILE, "onStart#start");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG_EDIT_PROFILE, "onResume#start");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG_EDIT_PROFILE, "onPause#start");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG_EDIT_PROFILE, "onStop#start");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG_EDIT_PROFILE, "onDestroy#start");
        super.onDestroy();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    @Override
    public void onDetach() {
        Log.d(TAG_EDIT_PROFILE, "onDetach#start");
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG_EDIT_PROFILE, "onDestroyView#start");
        super.onDestroyView();
        fragmentEditProfileBinding = null;
    }


}
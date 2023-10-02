package com.example.movieapp.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.movieapp.entity.User;
import com.example.movieapp.repository.UserRepository;
import com.example.movieapp.utils.CONSTANT;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public Flowable<List<User>> getAllUsers(){
        return userRepository.getAllUsers();
    }

    public Maybe<User> getFirstUser(){
        return userRepository.getFirstUser();
    }

    public Flowable<User> getUserById(Long idUser){
        return userRepository.getUserById(idUser);
    }

    public Completable insert(User user){
        Log.d(CONSTANT.TAG_USER_VIEW_MODEL, "insert(User user) = " + user.toString());

        return userRepository.insertUser(user);
    }
}

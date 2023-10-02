package com.example.movieapp.repository;

import android.app.Application;

import com.example.movieapp.database.MoviesDatabase;
import com.example.movieapp.database.UserDao;
import com.example.movieapp.entity.User;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Application application){
        MoviesDatabase db = MoviesDatabase.getInstance(application);
        userDao = db.userDao();
    }

    public Flowable<List<User>> getAllUsers(){
        return userDao.getAllUsers();
    }

    public Maybe<User> getFirstUser(){
        return userDao.getFirstUser();
    }

    public Flowable<User> getUserById(Long idUser){
        return userDao.getUserById(idUser);
    }

    public Completable insertUser(User user){
        return userDao.insert(user);
    }
}

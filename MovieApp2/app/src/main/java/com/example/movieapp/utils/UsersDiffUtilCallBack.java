package com.example.movieapp.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.example.movieapp.entity.User;

import java.util.ArrayList;

public class UsersDiffUtilCallBack extends DiffUtil.Callback {

    ArrayList<User> newUserList;
    ArrayList<User> oldUserList;

    public UsersDiffUtilCallBack(ArrayList<User> newUserList, ArrayList<User> oldUserList) {
        this.newUserList = newUserList;
        this.oldUserList = oldUserList;
    }

    @Override
    public int getOldListSize() {
        return oldUserList != null ? oldUserList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newUserList != null ? newUserList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newUserList.get(newItemPosition).idUser == oldUserList.get(oldItemPosition).idUser;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result =  newUserList.get(newItemPosition).compareTo(oldUserList.get(oldItemPosition));
        return result == 0;
    }
}

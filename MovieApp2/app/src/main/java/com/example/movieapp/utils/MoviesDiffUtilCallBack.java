package com.example.movieapp.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.example.movieapp.model.Movie;

import java.util.ArrayList;

public class MoviesDiffUtilCallBack extends DiffUtil.Callback {

    ArrayList<Movie> newMovieList;
    ArrayList<Movie> oldMovieList;

    public MoviesDiffUtilCallBack(ArrayList<Movie> newMovieList, ArrayList<Movie> oldMovieList) {
        this.newMovieList = newMovieList;
        this.oldMovieList = oldMovieList;
    }

    @Override
    public int getOldListSize() {
        return oldMovieList != null ? oldMovieList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newMovieList != null ? newMovieList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newMovieList.get(newItemPosition).getIdMovie() == oldMovieList.get(oldItemPosition).getIdMovie();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result =  newMovieList.get(newItemPosition).compareTo(oldMovieList.get(oldItemPosition));
        return result == 0;
    }
}

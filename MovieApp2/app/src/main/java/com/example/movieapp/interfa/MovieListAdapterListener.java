package com.example.movieapp.interfa;

import com.example.movieapp.model.Movie;

public interface MovieListAdapterListener {
    void openDetailOfAMovie(Movie movie, int position);
}

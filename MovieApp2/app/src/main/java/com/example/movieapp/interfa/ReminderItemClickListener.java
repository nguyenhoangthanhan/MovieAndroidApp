package com.example.movieapp.interfa;

import com.example.movieapp.model.Movie;

public interface ReminderItemClickListener {
    void onItemClick(Movie movie);
    void deleteReminderClick(Movie movie);
}

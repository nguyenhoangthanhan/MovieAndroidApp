package com.example.movieapp.interfa;

import com.example.movieapp.model.Movie;

import java.util.ArrayList;

public class DataDemo {

    public static ArrayList<Movie> getListMovieReminder() {
        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie(3, "Film 3", "", "2022-05-26", 7.5, true, false, "In onCreateViewHolder() method, post_row_item layout is inflated with the help of PostRowItemBinding class. holder.binding\n" +
                "        .setPost() binds the Post model to each row..setPost() binds the Post model to each row..setPost() binds the Post model to each row.", "2022/05/22 15:55"));
        movies.add(new Movie(4, "Film 4", "", "2022-05-26", 8.5, false, false, "In onCreateViewHolder() method, post_row_item layout is inflated with the help of PostRowItemBinding class. holder.binding\n" +
                "        .setPost() binds the Post model to each row..setPost() binds the Post model to each row..setPost() binds the Post model to each row.", "2020/07/11 08:30"));
        movies.add(new Movie(5, "Film 5", "", "2022-05-26", 9.5, true, false, "In onCreateViewHolder() method, post_row_item layout is inflated with the help of PostRowItemBinding class. holder.binding\n" +
                "        .setPost() binds the Post model to each row..setPost() binds the Post model to each row..setPost() binds the Post model to each row.", "2011/07/11 22:50"));
        movies.add(new Movie(6, "Film 6", "", "2022-05-26", 7.5, true, false, "In onCreateViewHolder() method, post_row_item layout is inflated with the help of PostRowItemBinding class. holder.binding\n" +
                "        .setPost() binds the Post model to each row..setPost() binds the Post model to each row..setPost() binds the Post model to each row.", "2023/07/11 14:10"));
        movies.add(new Movie(7, "Film 7", "", "2022-05-26", 5.5, true, false, "In onCreateViewHolder() method, post_row_item layout is inflated with the help of PostRowItemBinding class. holder.binding\n" +
                "        .setPost() binds the Post model to each row..setPost() binds the Post model to each row..setPost() binds the Post model to each row.", "2024/07/11 13:20"));
        return movies;
    }
}

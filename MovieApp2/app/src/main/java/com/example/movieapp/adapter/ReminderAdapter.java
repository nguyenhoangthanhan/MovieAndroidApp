package com.example.movieapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.databinding.LayoutMovieItemBinding;
import com.example.movieapp.databinding.LayoutMovieItemGridBinding;
import com.example.movieapp.databinding.LayoutReminderItemBinding;
import com.example.movieapp.interfa.MovieListAdapterListener;
import com.example.movieapp.interfa.ReminderItemClickListener;
import com.example.movieapp.model.Movie;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.MoviesDiffUtilCallBack;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Movie> movieList = new ArrayList<>();
    private final ReminderItemClickListener reminderItemClickListener;

    public ReminderAdapter(ReminderItemClickListener reminderItemClickListener) {
        this.reminderItemClickListener = reminderItemClickListener;
    }

    public void setData(ArrayList<Movie> newMovieList){
        Log.d(CONSTANT.TAG_MOVIE_ADAPTER, "setData#start");
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MoviesDiffUtilCallBack(newMovieList, movieList));
        diffResult.dispatchUpdatesTo(this);
        movieList.clear();
        this.movieList.addAll(newMovieList);
        Log.d(CONSTANT.TAG_MOVIE_ADAPTER, "setData#end");
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder{
        private final LayoutReminderItemBinding binding;

        public ReminderViewHolder(LayoutReminderItemBinding layoutReminderItemBinding) {
            super(layoutReminderItemBinding.getRoot());
            this.binding = layoutReminderItemBinding;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReminderViewHolder(LayoutReminderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        ReminderViewHolder reminderViewHolder = (ReminderViewHolder) holder;
        String infoFilm = movie.getTitle() + " - " + movie.getReleaseDate() + " - " + movie.getVoteAverage() +"/10";
        reminderViewHolder.binding.tvFilmInfo.setText(infoFilm);
        reminderViewHolder.binding.tvTimeReminder.setText(movie.getReminderDateTime());

        String urlImg = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Glide.with(reminderViewHolder.binding.imgMoviePoster.getContext()).load(urlImg)
                .placeholder(R.drawable.beginner3).dontAnimate().into(reminderViewHolder.binding.imgMoviePoster);

        reminderViewHolder.itemView.setOnClickListener(v -> reminderItemClickListener.onItemClick(movie));
        reminderViewHolder.itemView.setOnLongClickListener(v -> {
            reminderItemClickListener.deleteReminderClick(movie);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}

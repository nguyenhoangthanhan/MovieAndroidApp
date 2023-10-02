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
import com.example.movieapp.interfa.MovieListAdapterListener;
import com.example.movieapp.model.Movie;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.MoviesDiffUtilCallBack;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Movie> movieList = new ArrayList<>();
    private MovieListAdapterListener movieListAdapterListener = null;
    private FavouriteMovieListener favouriteMovieListener = null;

    public void setFavouriteMovieListener(FavouriteMovieListener favouriteMovieListener) {
        this.favouriteMovieListener = favouriteMovieListener;
    }

    public void setData(ArrayList<Movie> newMovieList){
        Log.d(CONSTANT.TAG_MOVIE_ADAPTER, "setData#start");
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MoviesDiffUtilCallBack(newMovieList, movieList));
        diffResult.dispatchUpdatesTo(this);
        movieList.clear();
        this.movieList.addAll(newMovieList);
        Log.d(CONSTANT.TAG_MOVIE_ADAPTER, "setData#end");
    }

    public MovieAdapter(MovieListAdapterListener movieListAdapterListener) {
        this.movieListAdapterListener = movieListAdapterListener;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        private final LayoutMovieItemBinding binding;
        public MovieViewHolder(LayoutMovieItemBinding layoutMovieItemBinding) {
            super(layoutMovieItemBinding.getRoot());
            this.binding = layoutMovieItemBinding;
        }
    }

    public static class GridMovieViewHolder extends RecyclerView.ViewHolder{
        private final LayoutMovieItemGridBinding binding;

        public GridMovieViewHolder(LayoutMovieItemGridBinding layoutMovieItemGridBinding) {
            super(layoutMovieItemGridBinding.getRoot());
            this.binding = layoutMovieItemGridBinding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = movieList.get(position);
        return movie.getTypeDisplay();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(CONSTANT.TAG_MOVIE_ADAPTER, "onCreateViewHolder#");
        if (viewType == Movie.TYPE_GRID) {
            return new GridMovieViewHolder(LayoutMovieItemGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
        return new MovieViewHolder(LayoutMovieItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(CONSTANT.TAG_MOVIE_ADAPTER, "onBindViewHolder#");
        switch (holder.getItemViewType()) {
            case Movie.TYPE_LIST:
            default:
                Movie movieViewType1 = movieList.get(position);
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                movieViewHolder.binding.tvFilmName.setText(movieViewType1.getTitle());
                movieViewHolder.binding.tvOverview.setText(movieViewType1.getOverview());
                String rating = movieViewType1.getVoteAverage() + "/10.0";
                movieViewHolder.binding.tvRating.setText(rating);
                movieViewHolder.binding.tvReleaseDate.setText(movieViewType1.getReleaseDate());
                String urlImg = "https://image.tmdb.org/t/p/w500" + movieViewType1.getPosterPath();
                Glide.with(movieViewHolder.binding.imgMoviePoster.getContext()).load(urlImg)
                        .placeholder(R.drawable.beginner3).dontAnimate().into(movieViewHolder.binding.imgMoviePoster);

                Log.d(CONSTANT.TAG_MOVIE_ADAPTER, "onBindViewHolder#movieViewType1.isAdultMovie() = " + movieViewType1.isAdultMovie());
                if (movieViewType1.isAdultMovie()){
                    movieViewHolder.binding.imgAdult.setVisibility(View.VISIBLE);
                }
                else{
                    movieViewHolder.binding.imgAdult.setVisibility(View.INVISIBLE);
                }

                if (movieViewType1.isFavouriteMovie()){
                    movieViewHolder.binding.imgStar.setImageResource(R.drawable.ic_yellow_star_24);
                }
                else{
                    movieViewHolder.binding.imgStar.setImageResource(R.drawable.ic_baseline_star_border_24);
                }

                if (movieListAdapterListener != null){
                    movieViewHolder.itemView.setOnClickListener(v -> movieListAdapterListener.openDetailOfAMovie(movieViewType1, position));
                }

                if (favouriteMovieListener != null){
                    movieViewHolder.binding.imgStar.setOnClickListener(v -> favouriteMovieListener.favouriteMovie(movieViewType1, position));
                }
                break;
            case Movie.TYPE_GRID:
                Movie movieViewType2 = movieList.get(position);
                GridMovieViewHolder movieViewHolderViewType2 = (GridMovieViewHolder) holder;
                movieViewHolderViewType2.binding.tvFilmName.setText(movieViewType2.getTitle());
                String urlImgViewType2 = "https://image.tmdb.org/t/p/w500" + movieViewType2.getPosterPath();
                Glide.with(movieViewHolderViewType2.binding.imgMoviePoster.getContext()).load(urlImgViewType2)
                        .placeholder(R.drawable.beginner3).dontAnimate().into(movieViewHolderViewType2.binding.imgMoviePoster);

                if (movieListAdapterListener != null) {
                    movieViewHolderViewType2.itemView.setOnClickListener(v -> movieListAdapterListener.openDetailOfAMovie(movieViewType2, position));
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface FavouriteMovieListener{
        void favouriteMovie(Movie movie, int position);
    }

}

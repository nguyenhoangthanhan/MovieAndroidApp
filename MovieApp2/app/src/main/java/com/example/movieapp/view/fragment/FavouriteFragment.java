package com.example.movieapp.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.MovieApplication;
import com.example.movieapp.R;
import com.example.movieapp.adapter.MovieAdapter;
import com.example.movieapp.databinding.FragmentFavouriteBinding;
import com.example.movieapp.entity.Favourite;
import com.example.movieapp.interfa.MovieListAdapterListener;
import com.example.movieapp.interfa.MovieListener;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;
import com.example.movieapp.view.fragment.fragment_movie.MovieDetailFragment;
import com.example.movieapp.view_model.FavouriteViewModel;
import com.example.movieapp.view_model.MovieViewModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouriteFragment extends Fragment implements MovieAdapter.FavouriteMovieListener
        , MovieListAdapterListener, MovieListener, MovieDetailFragment.DetailMovieFavouriteMovieListener {

    private FragmentFavouriteBinding fragmentFavouriteBinding;

    private final ArrayList<Movie> movieFavouriteArrayList = new ArrayList<>();
    private final ArrayList<Movie> movieFilterArrayList = new ArrayList<>();

    private MovieAdapter movieAdapter;

    private FavouriteViewModel favouriteViewModel;
    private MovieViewModel movieViewModel;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());

        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        movieAdapter = new MovieAdapter(this);
        movieAdapter.setFavouriteMovieListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentFavouriteBinding = FragmentFavouriteBinding.inflate(inflater, container, false);
        return fragmentFavouriteBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void init() {
        Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "init#start");
        movieAdapter.setData(movieFavouriteArrayList);
        fragmentFavouriteBinding.rvFavouriteMovies.setAdapter(movieAdapter);
        fragmentFavouriteBinding.rvFavouriteMovies.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void initData() {
        Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "initData#start");
        observerListFavouriteMovie();
        getAllFavouriteFromDB();
    }

    private void observerListFavouriteMovie() {
        Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "observerListFavouriteMovie#start");
        movieViewModel.getMovieGetById().observe(getViewLifecycleOwner(), movie -> {
            Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "observer-observerListFavouriteMovie");
            movieFavouriteArrayList.add(movie);
            movieAdapter.setData(movieFavouriteArrayList);
            movieAdapter.notifyDataSetChanged();
            Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "movieFavouriteArrayList.size() = " + movieFavouriteArrayList.size());
        });
    }

    private void getAllFavouriteFromDB() {
        Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "getAllFavouriteFromDB#start");
        compositeDisposable.add(favouriteViewModel.getAllFavourite(MovieApplication.idUserCurrent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                favourites -> {
                    movieFavouriteArrayList.clear();
                    Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "observer-getAllFavouriteFromDB");
                    for (int i = 0; i < favourites.size(); i++) {
                        if (favourites.get(i).isFavourite) {
                            getMoviesById(favourites.get(i).movieId);
                        }
                    }
                }, this::handleError)
        );

    }

    private void getMoviesById(int idMovie) {
        Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "getMoviesById#start");
        movieViewModel.getMovieDetail(idMovie, compositeDisposable);
    }

    private void handleError(Throwable error) {
        Log.e(CONSTANT.TAG_MOVIE_FRAGMENT, "getNowPlayingMovies: " + error.getMessage());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentFavouriteBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    @Override
    public void favouriteMovie(Movie movie, int position) {
        Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "favouriteMovie#start ");
        Favourite favourite = new Favourite(movie.getIdMovie(), false, MovieApplication.idUserCurrent);
        insertFavouriteToDB(favourite);
    }

    private void insertFavouriteToDB(Favourite favourite) {
        compositeDisposable.add(favouriteViewModel.insert(favourite).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());
    }

    public void filterFilm(CharSequence s) {
        if (s.equals("")){
            movieAdapter.setData(movieFavouriteArrayList);
        }
        else{
            movieFilterArrayList.clear();
            for (int i = 0; i < movieFavouriteArrayList.size(); i ++){
                if (movieFavouriteArrayList.get(i).getTitle().toLowerCase().contains(s.toString().toLowerCase())){
                    movieFilterArrayList.add(movieFavouriteArrayList.get(i));
                }
            }
            movieAdapter.setData(movieFilterArrayList);
        }
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void openDetailOfAMovie(Movie movie, int position) {

        Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "openDetailOfAMovie#start");
        FragmentManager fragmentManager = getParentFragmentManager();
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "movieDetailFragment#movie = " + movieDetailFragment);
        movieDetailFragment.setMovieListener(this);
        Bundle bundle = new Bundle();
        bundle.putInt(CONSTANT.KEY_CURRENT_POSITION, position);
        bundle.putParcelable(CONSTANT.KEY_SEND_MOVIE, movie);
        movieDetailFragment.setArguments(bundle);
        movieDetailFragment.setDetailMovieFavouriteMovieListener(this);
        fragmentTransaction.replace(R.id.container_favourite, movieDetailFragment, CONSTANT.OPEN_DETAIL_FRAGMENT);
        fragmentTransaction.addToBackStack(CONSTANT.BACK_FROM_DETAIL_FRAGMENT);
        fragmentTransaction.commit();

        Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "openDetailOfAMovie#end");
    }

    @Override
    public void changeTitleToolbar(String title) {

    }

    @Override
    public void detailMovieLikeMovie(int position) {

    }
}
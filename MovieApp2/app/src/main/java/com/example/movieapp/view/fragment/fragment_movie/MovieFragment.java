package com.example.movieapp.view.fragment.fragment_movie;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.MovieApplication;
import com.example.movieapp.R;
import com.example.movieapp.adapter.MovieAdapter;
import com.example.movieapp.databinding.FragmentMovieBinding;
import com.example.movieapp.entity.Favourite;
import com.example.movieapp.interfa.ChangeMovieDetailTitleToolbarListener;
import com.example.movieapp.interfa.ChangeMovieListTitleToolbarListener;
import com.example.movieapp.interfa.MovieListAdapterListener;
import com.example.movieapp.interfa.MovieListener;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;
import com.example.movieapp.view.fragment.MainFragment;
import com.example.movieapp.view_model.FavouriteViewModel;
import com.example.movieapp.view_model.MovieViewModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieFragment extends Fragment implements MovieListener, MovieListAdapterListener, MovieAdapter.FavouriteMovieListener
        , MovieDetailFragment.DetailMovieFavouriteMovieListener {

    private FavouriteViewModel favouriteViewModel;
    private MovieViewModel movieViewModel;

    private final ArrayList<Movie> moviePopularArrayList = new ArrayList<>();
    private final ArrayList<Movie> movieTopRatedArrayList = new ArrayList<>();
    private final ArrayList<Movie> movieUpcomingArrayList = new ArrayList<>();
    private final ArrayList<Movie> movieNowPlayingArrayList = new ArrayList<>();

    private final ArrayList<Movie> filterArrayList = new ArrayList<>();

    private MovieAdapter movieAdapter;

    private static final String TAG = "MovieFragment";

    private FragmentMovieBinding fragmentMovieBinding;

    private ChangeMovieDetailTitleToolbarListener changeMovieDetailTitleToolbarListener;
    private ChangeMovieListTitleToolbarListener changeMovieListTitleToolbarListener;

    public static int k1 = 1;
    public static int k2 = 1;
    public static int k3 = 1;
    public static int k4 = 1;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final CompositeDisposable compositeFavouriteDisposable = new CompositeDisposable();

    private int currentPositionItem = 0;

    private RecyclerView.LayoutManager linearLayoutManager;
    private RecyclerView.LayoutManager gridLayoutManager;

    public void setChangeMovieDetailTitleToolbarListener(ChangeMovieDetailTitleToolbarListener changeMovieDetailTitleToolbarListener) {
        this.changeMovieDetailTitleToolbarListener = changeMovieDetailTitleToolbarListener;
    }

    public void setChangeMovieListTitleToolbarListener(ChangeMovieListTitleToolbarListener changeMovieListTitleToolbarListener) {
        this.changeMovieListTitleToolbarListener = changeMovieListTitleToolbarListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());

        // Get a new or existing ViewModel from the ViewModelProvider.
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        linearLayoutManager = new LinearLayoutManager(requireContext());
        gridLayoutManager = new GridLayoutManager(requireContext(), 2);

        initDataAPI();
    }

    private void initDataAPI() {
        movieViewModel.getPopularMovies(k1, compositeDisposable);
        movieViewModel.getTopRatedMovies(k2, compositeDisposable);
        movieViewModel.getUpComingMovies(k3, compositeDisposable);
        movieViewModel.getNowPlayingMovies(k4, compositeDisposable);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMovieBinding = FragmentMovieBinding.inflate(inflater, container, false);
        return fragmentMovieBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        addEvents();

    }

    private void init() {
        movieAdapter = new MovieAdapter(this);
        movieAdapter.setData(moviePopularArrayList);
        movieAdapter.setFavouriteMovieListener(this);
        fragmentMovieBinding.rvMovieList.setAdapter(movieAdapter);

        if (MainFragment.isLinearLayoutVerticalShowMethod) {
            fragmentMovieBinding.rvMovieList.setLayoutManager(linearLayoutManager);
        } else {
            fragmentMovieBinding.rvMovieList.setLayoutManager(gridLayoutManager);
        }

        setPopularMoviesForView();
        setTopRatedMoviesForView();
        setUpcomingMoviesForView();
        setNowPlayingMoviesForView();

    }

    private void addEvents() {
        fragmentMovieBinding.rvMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    addNewListMovies(MainFragment.currentGenreSelected);
                    Log.d(TAG, "addEvents# k1 = " + k1);
                    Log.d(TAG, "addEvents# k2 = " + k2);
                    Log.d(TAG, "addEvents# k3 = " + k3);
                    Log.d(TAG, "addEvents# k4 = " + k4);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void setPopularMoviesForView() {
        Log.d(TAG, "setPopularMoviesForView#start");
        movieViewModel.getResultPopularMovies().observe(getViewLifecycleOwner(), movieArrayList -> {
            int oldCount = moviePopularArrayList.size();
            for (int i = 0; i < movieArrayList.size(); i++) {
                if (MainFragment.isLinearLayoutVerticalShowMethod) {
                    movieArrayList.get(i).setTypeDisplay(Movie.TYPE_LIST);
                } else {
                    movieArrayList.get(i).setTypeDisplay(Movie.TYPE_GRID);
                }
            }
            moviePopularArrayList.addAll(movieArrayList);
            setFavouritePopularMovies(oldCount);
            filterByRateFromAndYear(moviePopularArrayList);
        });
    }

    private void setTopRatedMoviesForView() {
        Log.d(TAG, "setTopRatedMoviesForView#start");
        movieViewModel.getResultTopRatedMovies().observe(getViewLifecycleOwner(), movieArrayList -> {
            int oldCount = movieTopRatedArrayList.size();
            for (int i = 0; i < movieArrayList.size(); i++) {
                if (MainFragment.isLinearLayoutVerticalShowMethod) {
                    movieArrayList.get(i).setTypeDisplay(Movie.TYPE_LIST);
                } else {
                    movieArrayList.get(i).setTypeDisplay(Movie.TYPE_GRID);
                }
            }
            movieTopRatedArrayList.addAll(movieArrayList);
            setFavouriteTopRatedMovies(oldCount);
            filterByRateFromAndYear(movieTopRatedArrayList);
        });
    }

    private void setUpcomingMoviesForView() {
        Log.d(TAG, "setUpcomingMoviesForView#start");
        movieViewModel.getResultUpComingMovies().observe(getViewLifecycleOwner(), movieArrayList -> {
            int oldCount = movieUpcomingArrayList.size();
            for (int i = 0; i < movieArrayList.size(); i++) {
                if (MainFragment.isLinearLayoutVerticalShowMethod) {
                    movieArrayList.get(i).setTypeDisplay(Movie.TYPE_LIST);
                } else {
                    movieArrayList.get(i).setTypeDisplay(Movie.TYPE_GRID);
                }
            }
            movieUpcomingArrayList.addAll(movieArrayList);
            setFavouriteUpcomingMovies(oldCount);
            filterByRateFromAndYear(movieUpcomingArrayList);
        });
    }

    private void setNowPlayingMoviesForView() {
        Log.d(TAG, "setNowPlayingMoviesForView#start");
        movieViewModel.getResultNowPlayingMovies().observe(getViewLifecycleOwner(), movieArrayList -> {
            int oldCount = movieNowPlayingArrayList.size();
            for (int i = 0; i < movieArrayList.size(); i++) {
                if (MainFragment.isLinearLayoutVerticalShowMethod) {
                    movieArrayList.get(i).setTypeDisplay(Movie.TYPE_LIST);
                } else {
                    movieArrayList.get(i).setTypeDisplay(Movie.TYPE_GRID);
                }
            }
            movieNowPlayingArrayList.addAll(movieArrayList);
            setFavouriteNowPlayingMovies(oldCount);
            filterByRateFromAndYear(movieNowPlayingArrayList);
        });
    }

    private void setFavouritePopularMovies(int oldSize) {
        compositeDisposable.add(favouriteViewModel.getAllFavourite(MovieApplication.idUserCurrent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                favourites -> {
                    Log.d(TAG, "setFavouritePopularMovies#start - observeOn(AndroidSchedulers.mainThread())");
                    for (int i = oldSize; i < moviePopularArrayList.size(); i++) {
                        for (int j = oldSize; j < favourites.size(); j++) {
                            if (moviePopularArrayList.get(i).getIdMovie() == favourites.get(j).movieId && favourites.get(j).isFavourite) {
                                moviePopularArrayList.get(i).setFavouriteMovie(true);
                                break;
                            } else {
                                moviePopularArrayList.get(i).setFavouriteMovie(false);
                            }
                        }
                    }
                }, this::handleError)
        );
    }

    private void setFavouriteTopRatedMovies(int oldSize) {
        compositeDisposable.add(favouriteViewModel.getAllFavourite(MovieApplication.idUserCurrent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                favourites -> {
                    Log.d(TAG, "setFavouriteTopRatedMovies#start - observeOn(AndroidSchedulers.mainThread())");
                    for (int i = oldSize; i < movieTopRatedArrayList.size(); i++) {
                        for (int j = oldSize; j < favourites.size(); j++) {
                            if (movieTopRatedArrayList.get(i).getIdMovie() == favourites.get(j).movieId && favourites.get(j).isFavourite) {
                                movieTopRatedArrayList.get(i).setFavouriteMovie(true);
                                break;
                            } else {
                                movieTopRatedArrayList.get(i).setFavouriteMovie(false);
                            }
                        }
                    }
                }, this::handleError)
        );
    }

    private void setFavouriteUpcomingMovies(int oldSize) {
        compositeDisposable.add(favouriteViewModel.getAllFavourite(MovieApplication.idUserCurrent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                favourites -> {
                    Log.d(TAG, "setFavouriteUpcomingMovies#start - observeOn(AndroidSchedulers.mainThread())");
                    for (int i = oldSize; i < movieUpcomingArrayList.size(); i++) {
                        for (int j = oldSize; j < favourites.size(); j++) {
                            if (movieUpcomingArrayList.get(i).getIdMovie() == favourites.get(j).movieId && favourites.get(j).isFavourite) {
                                movieUpcomingArrayList.get(i).setFavouriteMovie(true);
                                break;
                            } else {
                                movieUpcomingArrayList.get(i).setFavouriteMovie(false);
                            }
                        }
                    }
                }, this::handleError)
        );

    }

    private void setFavouriteNowPlayingMovies(int oldSize) {
        compositeDisposable.add(favouriteViewModel.getAllFavourite(MovieApplication.idUserCurrent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                favourites -> {
                    Log.d(TAG, "setFavouriteNowPlayingMovies#start - observeOn(AndroidSchedulers.mainThread())");
                    for (int i = oldSize; i < movieNowPlayingArrayList.size(); i++) {
                        for (int j = oldSize; j < favourites.size(); j++) {
                            if (movieNowPlayingArrayList.get(i).getIdMovie() == favourites.get(j).movieId && favourites.get(j).isFavourite) {
                                movieNowPlayingArrayList.get(i).setFavouriteMovie(true);
                                break;
                            } else {
                                movieNowPlayingArrayList.get(i).setFavouriteMovie(false);
                            }
                        }
                    }
                }, this::handleError)
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentMovieBinding = null;
    }

    @Override
    public void openDetailOfAMovie(Movie movie, int position) {
        Log.d(TAG, "openDetailOfAMovie#start");
        FragmentManager fragmentManager = getParentFragmentManager();
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Log.d(TAG, "movieDetailFragment#movie = " + movieDetailFragment);
        movieDetailFragment.setMovieListener(this);
        Bundle bundle = new Bundle();
        bundle.putInt(CONSTANT.KEY_CURRENT_POSITION, position);
        bundle.putParcelable(CONSTANT.KEY_SEND_MOVIE, movie);
        movieDetailFragment.setArguments(bundle);
        movieDetailFragment.setDetailMovieFavouriteMovieListener(this);
        fragmentTransaction.replace(R.id.container_movie, movieDetailFragment, CONSTANT.OPEN_DETAIL_FRAGMENT);
        fragmentTransaction.addToBackStack(CONSTANT.BACK_FROM_DETAIL_FRAGMENT);
        fragmentTransaction.commit();

        changeMovieDetailTitleToolbarListener.changeMovieDetailTitleToolbar(movie.getTitle());

        Log.d(TAG, "openDetailOfAMovie#end");
    }

    @Override
    public void changeTitleToolbar(String title) {
        changeMovieListTitleToolbarListener.changeMovieListTitleToolbar(title);
    }

    private void setTypeDisplayRCV(int typeDisplay) {
        ArrayList<Movie> movieArrayList = moviePopularArrayList;
        if (MainFragment.currentGenreSelected == CONSTANT.MOVIE_GENRE_TOP_RATED) {
            movieArrayList = movieTopRatedArrayList;
        } else if (MainFragment.currentGenreSelected == CONSTANT.MOVIE_GENRE_UPCOMING) {
            movieArrayList = movieUpcomingArrayList;
        } else if (MainFragment.currentGenreSelected == CONSTANT.MOVIE_GENRE_NOW_PLAYING) {
            movieArrayList = movieNowPlayingArrayList;
        }

        if (movieArrayList.isEmpty()) {
            return;
        }

        for (Movie movie : movieArrayList) {
            movie.setTypeDisplay(typeDisplay);
        }
    }

    public void onClickChangeTypeDisplay() {
        if (MainFragment.isLinearLayoutVerticalShowMethod) {
            setTypeDisplayRCV(Movie.TYPE_LIST);
            if (fragmentMovieBinding != null) {
                fragmentMovieBinding.rvMovieList.setLayoutManager(linearLayoutManager);
            }
        } else {
            setTypeDisplayRCV(Movie.TYPE_GRID);
            if (fragmentMovieBinding != null) {
                fragmentMovieBinding.rvMovieList.setLayoutManager(gridLayoutManager);
            }
        }
    }

    public void addNewListMovies(int currentGenreSelected) {
        if (currentGenreSelected == CONSTANT.MOVIE_GENRE_POPULAR) {
            movieViewModel.getPopularMovies(++k1, compositeDisposable);
        } else if (currentGenreSelected == CONSTANT.MOVIE_GENRE_TOP_RATED) {
            movieViewModel.getTopRatedMovies(++k2, compositeDisposable);
        } else if (currentGenreSelected == CONSTANT.MOVIE_GENRE_UPCOMING) {
            movieViewModel.getUpComingMovies(++k3, compositeDisposable);
        } else if (currentGenreSelected == CONSTANT.MOVIE_GENRE_NOW_PLAYING) {
            movieViewModel.getNowPlayingMovies(++k4, compositeDisposable);
        }
    }

    public void setMoviesDataShowed(int currentGenreSelected) {
        Log.d(CONSTANT.TAG_MOVIE_FRAGMENT, "setMoviesDataShowed - MainFragment.currentGenreSelected : " + MainFragment.currentGenreSelected);
        if (currentGenreSelected == CONSTANT.MOVIE_GENRE_POPULAR) {
            filterByRateFromAndYear(moviePopularArrayList);
            onClickChangeTypeDisplay();
        } else if (currentGenreSelected == CONSTANT.MOVIE_GENRE_TOP_RATED) {
            filterByRateFromAndYear(movieTopRatedArrayList);
            onClickChangeTypeDisplay();
        } else if (currentGenreSelected == CONSTANT.MOVIE_GENRE_UPCOMING) {
            filterByRateFromAndYear(movieUpcomingArrayList);
            onClickChangeTypeDisplay();
        } else if (currentGenreSelected == CONSTANT.MOVIE_GENRE_NOW_PLAYING) {
            filterByRateFromAndYear(movieNowPlayingArrayList);
            onClickChangeTypeDisplay();
        }
    }

    private void filterByRateFromAndYear(ArrayList<Movie> movieArrayList) {
        Log.d(CONSTANT.TAG_MOVIE_FRAGMENT, "filterByRateFromAndYear#start ");
        filterArrayList.clear();
        int rate = Integer.parseInt(SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_MOVIE_WITH_RATE_FROM, getString(R.string.rate_demo)));
        int yearFrom = Integer.parseInt(SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_YEAR_FROM, getString(R.string.year_demo)));
        String sortType = SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_SORT_BY, getString(R.string.release_date_normal));
        for (int i = 0; i < movieArrayList.size(); i++) {
            if (movieArrayList.get(i).getVoteAverage() >= rate && Utils.getYear(movieArrayList.get(i).getReleaseDate()) >= yearFrom) {
                filterArrayList.add(movieArrayList.get(i));
            }
        }

        if (sortType.equals(getString(R.string.release_date_normal))) {
            sortReleaseDateDesc(filterArrayList);
        } else {
            sortRateDesc(filterArrayList);
        }

        movieAdapter.setData(filterArrayList);
        movieAdapter.notifyDataSetChanged();
        Log.d(CONSTANT.TAG_MOVIE_FRAGMENT, "filterByRateFromAndYear#end");
    }

    private void sortRateDesc(ArrayList<Movie> movieArrayList) {
        Log.d(CONSTANT.TAG_MOVIE_FRAGMENT, "sortReleaseDateDesc#start");
        Collections.sort(movieArrayList, (o1, o2) -> (int) (o1.getVoteAverage() * 10) - (int) (o2.getVoteAverage() * 10));
        Log.d(CONSTANT.TAG_MOVIE_FRAGMENT, "sortReleaseDateDesc#end");
    }

    private void sortReleaseDateDesc(ArrayList<Movie> movieArrayList) {
        Log.d(CONSTANT.TAG_MOVIE_FRAGMENT, "sortRateDesc#start ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(movieArrayList, Comparator.comparing(Movie::getReleaseDate));
        } else {
            Collections.sort(movieArrayList, (o1, o2) -> {

                Calendar calendarO1 = Calendar.getInstance();
                try {
                    String timePush = o1.getReleaseDate();
                    calendarO1.setTime(Utils.getFormatReleaseDate().parse(timePush));
                } catch (ParseException e) {
                    calendarO1.setTimeInMillis(0);
                    Log.e(CONSTANT.LOG_NOTIFICATION, e.getMessage());
                }

                Calendar calendarO2 = Calendar.getInstance();
                try {
                    String timePush = o2.getReleaseDate();
                    calendarO2.setTime(Utils.getFormatReleaseDate().parse(timePush));
                } catch (ParseException e) {
                    calendarO2.setTimeInMillis(0);
                    Log.e(CONSTANT.LOG_NOTIFICATION, e.getMessage());
                }

                return Long.compare(calendarO1.getTimeInMillis(), calendarO2.getTimeInMillis());
            });

            Collections.reverse(movieArrayList);
        }
        Log.d(CONSTANT.TAG_MOVIE_FRAGMENT, "sortRateDesc#end");
    }

    @Override
    public void favouriteMovie(Movie movie, int position) {
        Log.d(CONSTANT.TAG_MOVIE_FRAGMENT, "favouriteMovie#start ");
        Log.d(CONSTANT.TAG_MOVIE_FRAGMENT, "favouriteMovie#start. movie.isFavouriteMovie() = " + movie.isFavouriteMovie());
        Favourite favourite;
        if (movie.isFavouriteMovie()) {
            favourite = new Favourite(movie.getIdMovie(), false, MovieApplication.idUserCurrent);
        } else {
            favourite = new Favourite(movie.getIdMovie(), true, MovieApplication.idUserCurrent);
        }
        insertFavouriteToDB(favourite);
        setFavourite(movie, position);
    }

    private void insertFavouriteToDB(Favourite favourite) {
        compositeDisposable.add(favouriteViewModel.insert(favourite).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());
    }

    private void setFavourite(Movie movie, int position) {
        Log.d(TAG, "setFavourite#start");
        if (currentPositionItem != position) {
            compositeFavouriteDisposable.clear();
        }
        compositeFavouriteDisposable.add(favouriteViewModel.getFavouriteById(movie.getIdMovie(), MovieApplication.idUserCurrent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                favourite -> {
                    currentPositionItem = position;
                    movie.setFavouriteMovie(favourite.isFavourite);
                    Log.d(TAG, "setFavourite#mMovie mMovie.isFavouriteMovie() after reset = " + movie.isFavouriteMovie());
                    movieAdapter.notifyItemChanged(position);
                })
        );
    }

    @Override
    public void detailMovieLikeMovie(int position) {
        movieAdapter.notifyItemChanged(position);
    }

    private void handleError(Throwable error) {
        Log.e(CONSTANT.TAG_MOVIE_FRAGMENT, "getNowPlayingMovies: " + error.getMessage());
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
        compositeFavouriteDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        compositeFavouriteDisposable.dispose();
    }
}

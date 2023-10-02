package com.example.movieapp.view.fragment.fragment_movie;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.movieapp.MovieApplication;
import com.example.movieapp.R;
import com.example.movieapp.adapter.CastAndCrewAdapter;
import com.example.movieapp.databinding.FragmentMovieDetailBinding;
import com.example.movieapp.entity.Favourite;
import com.example.movieapp.entity.Reminder;
import com.example.movieapp.interfa.MovieListener;
import com.example.movieapp.model.Cast;
import com.example.movieapp.model.Crew;
import com.example.movieapp.model.ItemCastAndCrew;
import com.example.movieapp.model.Movie;
import com.example.movieapp.utils.AlarmUtils;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;
import com.example.movieapp.view.fragment.MainFragment;
import com.example.movieapp.view_model.FavouriteViewModel;
import com.example.movieapp.view_model.MovieViewModel;
import com.example.movieapp.view_model.ReminderViewModel;
import com.example.movieapp.view_model.ViewModelManager;

import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailFragment extends Fragment implements View.OnClickListener, CastAndCrewAdapter.OpenDetailPersonClickListener {

    private FavouriteViewModel favouriteViewModel;
    private MovieViewModel movieViewModel;
    private ReminderViewModel reminderViewModel;

    private static final String TAG = "MovieDetailFragment";

    private FragmentMovieDetailBinding fragmentMovieDetailBinding;

    private Movie mMovie;
    private int positionMMovie = -1;

    private MovieListener movieListener;

    private CastAndCrewAdapter castAndCrewAdapter;

    private DetailMovieFavouriteMovieListener detailMovieFavouriteMovieListener;

    private final CompositeDisposable compositeDetailMovie = new CompositeDisposable();

    private final ArrayList<ItemCastAndCrew> itemCastAndCrewArrayList = new ArrayList<>();

    private final Calendar calendar = Calendar.getInstance();

    public void setDetailMovieFavouriteMovieListener(DetailMovieFavouriteMovieListener detailMovieFavouriteMovieListener) {
        this.detailMovieFavouriteMovieListener = detailMovieFavouriteMovieListener;
    }

    public void setMovieListener(MovieListener movieListener) {
        this.movieListener = movieListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate#start");
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());

        // Get a new or existing ViewModel from the ViewModelProvider.
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mMovie = bundle.getParcelable(CONSTANT.KEY_SEND_MOVIE);

            positionMMovie = bundle.getInt(CONSTANT.KEY_CURRENT_POSITION);
        }
        movieViewModel.getCastAndCrews(mMovie.getIdMovie(), compositeDetailMovie);
        Log.d(TAG, "onCreate#end");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMovieDetailBinding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        return fragmentMovieDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        setFavourite();

        setCastAndCrews();

        addEvents();
    }

    private void init() {
        MainFragment.isMovieFragment = false;
        MainFragment.titleCurrentMovie = mMovie.getTitle();
        fragmentMovieDetailBinding.tvReleaseDate.setText(mMovie.getReleaseDate());
        String rating = mMovie.getVoteAverage() + "/10";
        fragmentMovieDetailBinding.tvRating.setText(rating);
        fragmentMovieDetailBinding.tvOverview.setText(mMovie.getOverview());

        String urlImg = "https://image.tmdb.org/t/p/w500" + mMovie.getPosterPath();
        Glide.with(requireContext()).load(urlImg).placeholder(R.drawable.beginner3).dontAnimate().into(fragmentMovieDetailBinding.imgFilmPoster);

        if (mMovie.isAdultMovie()) {
            fragmentMovieDetailBinding.imgAdult.setVisibility(View.VISIBLE);
        } else {
            fragmentMovieDetailBinding.imgAdult.setVisibility(View.INVISIBLE);
        }

        castAndCrewAdapter = new CastAndCrewAdapter(itemCastAndCrewArrayList);
        castAndCrewAdapter.setOpenDetailPersonClickListener(this);
        fragmentMovieDetailBinding.rvCastAndCrew.setAdapter(castAndCrewAdapter);
        fragmentMovieDetailBinding.rvCastAndCrew.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setFavourite() {
        Log.d(TAG, "setFavourite#start");
        compositeDetailMovie.add(favouriteViewModel.getFavouriteById(mMovie.getIdMovie(), MovieApplication.idUserCurrent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                favourite -> {
                    mMovie.setFavouriteMovie(favourite.isFavourite);
                    if (mMovie.isFavouriteMovie()) {
                        fragmentMovieDetailBinding.imgStar.setImageResource(R.drawable.ic_yellow_star_24);
                    } else {
                        fragmentMovieDetailBinding.imgStar.setImageResource(R.drawable.ic_baseline_star_border_24);
                    }

                })
        );
        Log.d(TAG, "setFavourite#end");
    }

    private void setCastAndCrews() {
        Log.d(TAG, "setCastAndCrews#start");
        movieViewModel.getCastCrewMoviesLiveData().observe(getViewLifecycleOwner(), castCrewMovies -> {
            itemCastAndCrewArrayList.clear();
            ArrayList<Cast> castArrayList = (ArrayList<Cast>) castCrewMovies.getCast();
            ArrayList<Crew> crewArrayList = (ArrayList<Crew>) castCrewMovies.getCrew();
            for (int i = 0; i < castArrayList.size(); i++) {
                itemCastAndCrewArrayList.add(new ItemCastAndCrew(castArrayList.get(i).getId(), castArrayList.get(i).getName(), castArrayList.get(i).getProfilePath()));
            }
            for (int i = 0; i < crewArrayList.size(); i++) {
                itemCastAndCrewArrayList.add(new ItemCastAndCrew(crewArrayList.get(i).getId(), crewArrayList.get(i).getName(), crewArrayList.get(i).getProfilePath()));
            }
            castAndCrewAdapter.notifyDataSetChanged();
        });
        Log.d(TAG, "setCastAndCrews#end");
    }

    private void addEvents() {
        fragmentMovieDetailBinding.imgStar.setOnClickListener(this);
        fragmentMovieDetailBinding.btnReminder.setOnClickListener(this);
    }

    private void favouriteMovie() {
        Log.d(CONSTANT.TAG_MOVIE_DETAIL_FRAGMENT, "favouriteMovie#start ");
        Favourite favourite;
        if (mMovie.isFavouriteMovie()) {
            Log.d(CONSTANT.TAG_MOVIE_DETAIL_FRAGMENT, "favouriteMovie#start. movie.isFavouriteMovie() = " + mMovie.isFavouriteMovie());
            favourite = new Favourite(mMovie.getIdMovie(), false, MovieApplication.idUserCurrent);
        } else {
            Log.d(CONSTANT.TAG_MOVIE_DETAIL_FRAGMENT, "favouriteMovie#start. movie.isFavouriteMovie() = " + mMovie.isFavouriteMovie());
            favourite = new Favourite(mMovie.getIdMovie(), true, MovieApplication.idUserCurrent);
        }
        insertFavouriteToDB(favourite);
    }

    private void createReminderThisMovie() {
        Log.d(CONSTANT.TAG_MOVIE_DETAIL_FRAGMENT, "createReminderThisMovie#start");
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), datePickerListener, mYear, mMonth, mDay);
        dateDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        dateDialog.show();
        Log.d(CONSTANT.TAG_MOVIE_DETAIL_FRAGMENT, "createReminderThisMovie#end");
    }

    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            TimePickerDialog.OnTimeSetListener timeSetListener = (view1, hourOfDay, minute) -> {
                calendar.set(year, month, dayOfMonth, hourOfDay, minute);
                long currentTimeMillis = Calendar.getInstance().getTimeInMillis();
                long addedTimeMillis = calendar.getTimeInMillis();
                if (addedTimeMillis > currentTimeMillis) {
                    String timeReminder = Utils.getFormatFull().format(calendar.getTime());
                    fragmentMovieDetailBinding.tvTimeReminder.setText(timeReminder);

                    compositeDetailMovie.add(reminderViewModel.insertReminder(new Reminder(mMovie.getIdMovie(), timeReminder, MovieApplication.idUserCurrent))
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                setNotification(mMovie.getIdMovie(), timeReminder);
                            }));
                }
            };
            new TimePickerDialog(requireContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        }
    };

    private void insertFavouriteToDB(Favourite favourite) {
        compositeDetailMovie.add(favouriteViewModel.insert(favourite).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe());
    }

    private void setNotification(int idMovie, String timeReminder) {
        Log.d(CONSTANT.LOG_NOTIFICATION, "setNotification-TAG_FAVOURITE_FRAGMENT-#start");
        compositeDetailMovie.add(movieViewModel.getMovieDetailByRxJava(idMovie)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        movie -> {
                            movie.setReminderDateTime(timeReminder);
                            AlarmUtils.create(requireContext(), movie, idMovie);

                        }, ViewModelManager::handleError, ViewModelManager::handleSuccess)
        );

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_star) {
            favouriteMovie();
            setFavourite();
        }
        if (v.getId() == R.id.btn_reminder) {
            createReminderThisMovie();
        }
    }

    @Override
    public void openDetailPersonClick() {
        Log.d(CONSTANT.TAG_MOVIE_DETAIL_FRAGMENT, "openDetailPersonClick#start");
        FragmentManager fragmentManager = getParentFragmentManager();
        ViewPager2PersonsFragment viewPager2PersonsFragment = new ViewPager2PersonsFragment();
        viewPager2PersonsFragment.setItemCastAndCrewArrayList(itemCastAndCrewArrayList);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_main, viewPager2PersonsFragment, CONSTANT.OPEN_DETAIL_PERSON_FRAGMENT);
        fragmentTransaction.addToBackStack(CONSTANT.BACK_FROM_DETAIL_PERSON_FRAGMENT);
        fragmentTransaction.commit();
        Log.d(CONSTANT.TAG_MOVIE_DETAIL_FRAGMENT, "openDetailPersonClick#end");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause#start");
        super.onPause();
        Log.d(TAG, "onPause#end");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop#start");
        super.onStop();
        Log.d(TAG, "onStop#end");
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView#start");
        super.onDestroyView();
        fragmentMovieDetailBinding = null;
        Log.d(TAG, "onDestroyView#end");
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach#start");
        super.onDetach();
        Log.d(TAG, "onDetach#end");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy#start");
        super.onDestroy();
        if (movieListener != null) {
            movieListener.changeTitleToolbar(getString(R.string.movies));
        }
        MainFragment.isMovieFragment = true;
        if (detailMovieFavouriteMovieListener != null) {
            detailMovieFavouriteMovieListener.detailMovieLikeMovie(positionMMovie);
        }
        compositeDetailMovie.clear();
        compositeDetailMovie.dispose();
        Log.d(TAG, "onDestroy#end");
    }

    public interface DetailMovieFavouriteMovieListener {
        void detailMovieLikeMovie(int position);
    }
}

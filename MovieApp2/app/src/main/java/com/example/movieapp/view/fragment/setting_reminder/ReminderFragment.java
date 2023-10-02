package com.example.movieapp.view.fragment.setting_reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieapp.MovieApplication;
import com.example.movieapp.R;
import com.example.movieapp.adapter.ReminderAdapter;
import com.example.movieapp.databinding.FragmentReminderBinding;
import com.example.movieapp.interfa.ReminderItemClickListener;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.receiver.MovieNotificationReceiver;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;
import com.example.movieapp.view.dialog.CategoryMoviesDialog;
import com.example.movieapp.view.dialog.DeleteReminderDialog;
import com.example.movieapp.view.fragment.fragment_movie.MovieDetailFragment;
import com.example.movieapp.view_model.MovieViewModel;
import com.example.movieapp.view_model.ReminderViewModel;
import com.example.movieapp.view_model.ViewModelManager;
import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReminderFragment extends Fragment implements ReminderItemClickListener, View.OnLongClickListener, DeleteReminderDialog.DeleteReminderClickListener {

    private ReminderViewModel reminderViewModel;
    private MovieViewModel movieViewModel;

    private FragmentReminderBinding fragmentReminderBinding;

    private ReminderAdapter reminderAdapter;

    private final ArrayList<Movie> movieReminderArrayList = new ArrayList<>();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int idMovieCurrent = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        reminderViewModel.getAllReminder(compositeDisposable, MovieApplication.idUserCurrent);
        reminderViewModel.getAllListIdMovieDistinct(compositeDisposable);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentReminderBinding = FragmentReminderBinding.inflate(inflater, container, false);
        return fragmentReminderBinding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(CONSTANT.TAG_REMINDER_FRAGMENT, "onViewCreated#start");
        super.onViewCreated(view, savedInstanceState);

        init();

        initData();
    }

    private void init() {
        reminderAdapter = new ReminderAdapter(this);
        fragmentReminderBinding.rvReminderList.setAdapter(reminderAdapter);
        fragmentReminderBinding.rvReminderList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void initData() {
        Log.d(CONSTANT.TAG_REMINDER_FRAGMENT, "initData#start");
        compositeDisposable.add(reminderViewModel.getAllReminderRx(MovieApplication.idUserCurrent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                reminders->{
                    movieReminderArrayList.clear();
                    for (int j = 0; j < reminders.size(); j++){
                        int finalJ = j;
                        compositeDisposable.add(movieViewModel.getMovieDetailByRxJava(reminders.get(j).movieId)
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                        movie -> {
                                            movie.setReminderDateTime(reminders.get(finalJ).reminderTime);
                                            movieReminderArrayList.add(movie);
                                            reminderAdapter.setData(movieReminderArrayList);
                                            reminderAdapter.notifyDataSetChanged();
                                        }, ViewModelManager::handleError, ViewModelManager::handleSuccess)
                        );
                    }
                }, ViewModelManager::handleError
        ));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentReminderBinding = null;
    }

    @Override
    public void onItemClick(Movie movie) {
        Log.d(CONSTANT.TAG_REMINDER_FRAGMENT, "onItemClick#start");
        FragmentManager fragmentManager = getParentFragmentManager();
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CONSTANT.KEY_SEND_MOVIE, movie);
        movieDetailFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container_reminder, movieDetailFragment, CONSTANT.OPEN_DETAIL_FRAGMENT);
        fragmentTransaction.addToBackStack(CONSTANT.BACK_FROM_DETAIL_FRAGMENT);
        fragmentTransaction.commit();

        Log.d(CONSTANT.TAG_REMINDER_FRAGMENT, "onItemClick#end");
    }

    @Override
    public void deleteReminderClick(Movie movie) {
        idMovieCurrent = movie.getIdMovie();
        showDialogDeleteReminder();
    }

    private void showDialogDeleteReminder() {
        DeleteReminderDialog deleteReminderDialog = new DeleteReminderDialog();
        deleteReminderDialog.setDeleteReminderClickListener(this);
        deleteReminderDialog.show(getChildFragmentManager(), CONSTANT.TAG_DIALOG_FILM_CATEGORY);
    }

    private void cancelAlarm(int idMovieCurrent) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(requireContext().getApplicationContext(), MovieNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext().getApplicationContext(), idMovieCurrent, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void deleteReminderDialog() {
        cancelAlarm(idMovieCurrent);
        reminderViewModel.deleteReminderByIdMovie(idMovieCurrent, MovieApplication.idUserCurrent, compositeDisposable);
    }
}

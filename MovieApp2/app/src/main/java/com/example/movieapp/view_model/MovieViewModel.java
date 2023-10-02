package com.example.movieapp.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.model.CastCrewMovies;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.Person;
import com.example.movieapp.repository.GetMoviesRepository;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieViewModel extends ViewModel {

    private final GetMoviesRepository getMoviesRepository;

    public MovieViewModel() {
        getMoviesRepository = new GetMoviesRepository();
    }

    private final MutableLiveData<ArrayList<Movie>> resultPopularMovies = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Movie>> resultTopRatedMovies = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Movie>> resultUpComingMovies = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Movie>> resultNowPlayingMovies = new MutableLiveData<>();
    private final MutableLiveData<Movie> movieGetById = new MutableLiveData<>();
    private final MutableLiveData<CastCrewMovies> castCrewMoviesLiveData = new MutableLiveData<>();

    public void getPopularMovies(int pageNumber, CompositeDisposable compositeDisposable){
        compositeDisposable.add(getMoviesRepository.getPopularMovies(pageNumber).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                result -> resultPopularMovies.setValue(result.getMovies()), ViewModelManager::handleError, ViewModelManager::handleSuccess)
        );
    }

    public void getTopRatedMovies(int pageNumber, CompositeDisposable compositeDisposable){
        compositeDisposable.add(getMoviesRepository.getTopRatedMovies(pageNumber).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                result -> resultTopRatedMovies.setValue(result.getMovies()), ViewModelManager::handleError, ViewModelManager::handleSuccess)
        );
    }

    public void getUpComingMovies(int pageNumber, CompositeDisposable compositeDisposable){
        compositeDisposable.add(getMoviesRepository.getUpComingMovies(pageNumber).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                result -> resultUpComingMovies.setValue(result.getMovies()), ViewModelManager::handleError, ViewModelManager::handleSuccess)
        );
    }

    public void getNowPlayingMovies(int pageNumber, CompositeDisposable compositeDisposable){
        compositeDisposable.add(getMoviesRepository.getNowPlayingMovies(pageNumber).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                result -> resultNowPlayingMovies.setValue(result.getMovies()), ViewModelManager::handleError, ViewModelManager::handleSuccess)
        );
    }

    public void getMovieDetail(int idMovie, CompositeDisposable compositeDisposable){
        compositeDisposable.add(getMoviesRepository.getMovieDetail(idMovie).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                movie ->{
                    movie.setTypeDisplay(Movie.TYPE_LIST);
                    movie.setFavouriteMovie(true);
                    movieGetById.setValue(movie);
                }, ViewModelManager::handleError, ViewModelManager::handleSuccess)
        );
    }

    public Flowable<Movie> getMovieDetailByRxJava(int idMovie){
        return getMoviesRepository.getMovieDetail(idMovie);
    }

    public Flowable<Person> getPersonDetailByRxJava(int idPerson){
        return getMoviesRepository.getPersonDetail(idPerson);
    }

    public void getCastAndCrews(int idMovie, CompositeDisposable compositeDisposable){
        compositeDisposable.add(getMoviesRepository.getCastAndCrews(idMovie).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                castCrewMoviesLiveData::setValue, ViewModelManager::handleError, ViewModelManager::handleSuccess)
        );
    }

    public MutableLiveData<ArrayList<Movie>> getResultPopularMovies() {
        return resultPopularMovies;
    }

    public MutableLiveData<ArrayList<Movie>> getResultTopRatedMovies() {
        return resultTopRatedMovies;
    }

    public MutableLiveData<ArrayList<Movie>> getResultUpComingMovies() {
        return resultUpComingMovies;
    }

    public MutableLiveData<ArrayList<Movie>> getResultNowPlayingMovies() {
        return resultNowPlayingMovies;
    }

    public MutableLiveData<Movie> getMovieGetById() {
        return movieGetById;
    }

    public MutableLiveData<CastCrewMovies> getCastCrewMoviesLiveData() {
        return castCrewMoviesLiveData;
    }
}

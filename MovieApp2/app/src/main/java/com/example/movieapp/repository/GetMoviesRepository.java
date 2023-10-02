package com.example.movieapp.repository;

import android.util.Log;

import com.example.movieapp.model.CastCrewMovies;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.Person;
import com.example.movieapp.model.Result;
import com.example.movieapp.network.API;
import com.example.movieapp.network.ApiService;
import com.example.movieapp.utils.CONSTANT;
import io.reactivex.rxjava3.core.Flowable;

public class GetMoviesRepository {
    private final ApiService apiService;

    public GetMoviesRepository() {
        this.apiService = API.getRetrofit().create(ApiService.class);
    }

    public Flowable<Result> getPopularMovies(int pageNumber){
        Log.d(CONSTANT.TAG_REPOSITORY, "getPopularMovies");
        return apiService.getPopularMovies(CONSTANT.API_KEY, pageNumber);
    }

    public Flowable<Result> getTopRatedMovies(int pageNumber){
        Log.d(CONSTANT.TAG_REPOSITORY, "getTopRatedMovies");
        return apiService.getTopRatingMovies(CONSTANT.API_KEY, pageNumber);
    }

    public Flowable<Result> getUpComingMovies(int pageNumber){
        Log.d(CONSTANT.TAG_REPOSITORY, "getUpComingMovies");
        return apiService.getUpcomingMovies(CONSTANT.API_KEY, pageNumber);
    }

    public Flowable<Result> getNowPlayingMovies(int pageNumber){
        Log.d(CONSTANT.TAG_REPOSITORY, "getNowPlayingMovies");
        return apiService.getNowPlayingMovies(CONSTANT.API_KEY, pageNumber);
    }

    public Flowable<Movie> getMovieDetail(int idMovie){
        Log.d(CONSTANT.TAG_REPOSITORY, "getMovieDetail");
        return apiService.getMovieDetail(idMovie, CONSTANT.API_KEY);
    }

    public Flowable<Person> getPersonDetail(int idPerson){
        Log.d(CONSTANT.TAG_REPOSITORY, "getPersonDetail");
        return apiService.getDetailPerson(idPerson, CONSTANT.API_KEY);
    }

    public Flowable<CastCrewMovies> getCastAndCrews(int idMovie){
        Log.d(CONSTANT.TAG_REPOSITORY, "getCastAndCrews");
        return apiService.getCastAndCrews(idMovie, CONSTANT.API_KEY);
    }
}

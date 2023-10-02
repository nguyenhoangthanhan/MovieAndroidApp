package com.example.movieapp.network;

import com.example.movieapp.model.CastCrewMovies;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.Person;
import com.example.movieapp.model.Result;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/3/movie/popular")
    Flowable<Result> getPopularMovies(@Query("api_key") String api_key, @Query("page") int pageNumber);

    @GET("/3/movie/top_rated")
    Flowable<Result> getTopRatingMovies(@Query("api_key")String key, @Query("page") int pageNumber);

    @GET("/3/movie/upcoming")
    Flowable<Result> getUpcomingMovies(@Query("api_key")String key, @Query("page") int pageNumber);

    @GET("/3/movie/now_playing")
    Flowable<Result> getNowPlayingMovies(@Query("api_key")String key, @Query("page") int pageNumber);

    @GET("/3/movie/{id}")
    Flowable<Movie> getMovieDetail(@Path("id") int movieId, @Query("api_key")String key);

    @GET("/3/movie/{id}/credits")
    Flowable<CastCrewMovies> getCastAndCrews(@Path("id") int movieId, @Query("api_key")String key);

    @GET("/3/person/{persionId}")
    Flowable<Person> getDetailPerson(@Path("persionId") int personId, @Query("api_key")String key);

    @GET("/t/p/{size}/{id}")
    Flowable<Result> getImage(@Path("id") int movieId, @Path("size") int size);
}
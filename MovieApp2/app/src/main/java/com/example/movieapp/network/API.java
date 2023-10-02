package com.example.movieapp.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
public final class API {
    private static final String baseUrl = "https://api.themoviedb.org/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit(){

        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl).addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

}

package com.example.movieapp.view_model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.entity.Favourite;
import com.example.movieapp.repository.FavouriteRepository;
import com.example.movieapp.utils.CONSTANT;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouriteViewModel  extends AndroidViewModel {
    private final FavouriteRepository getFavouriteRepository;
    private final MutableLiveData<List<Favourite>> favouriteListLiveData = new MutableLiveData<>();

    public FavouriteViewModel(Application application){
        super(application);
        getFavouriteRepository = new FavouriteRepository(application);
    }

    public Flowable<List<Favourite>> getAllFavourite(Long idUser){
        return getFavouriteRepository.getAllFavourite(idUser);
    }

    public Flowable<Favourite> getFavouriteById(int idMovie, Long idUser){
        return getFavouriteRepository.getFavouriteById(idMovie, idUser);
    }

    public Completable insert(Favourite favourite){
        return Completable.fromAction( ()-> getFavouriteRepository.insertFavourite(favourite));
    }

    public void getAllFavouriteForLiveData(CompositeDisposable compositeDisposable, Long idUser){
        compositeDisposable.add(getFavouriteRepository.getAllFavourite(idUser).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                favouriteListLiveData::setValue, ViewModelManager::handleError)
        );
    }

    public MutableLiveData<List<Favourite>> getFavouriteListLiveData() {
        return favouriteListLiveData;
    }
}

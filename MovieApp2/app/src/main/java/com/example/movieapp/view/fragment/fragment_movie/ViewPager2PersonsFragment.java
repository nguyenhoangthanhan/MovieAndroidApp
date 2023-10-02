package com.example.movieapp.view.fragment.fragment_movie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieapp.databinding.FragmentViewpager2PersonBinding;
import com.example.movieapp.model.ItemCastAndCrew;
import com.example.movieapp.model.Person;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.Utils;
import com.example.movieapp.view_model.MovieViewModel;
import com.example.movieapp.view_model.ViewModelManager;
import com.example.movieapp.viewpager_adapter.FmPersonsViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ViewPager2PersonsFragment extends Fragment {

    private MovieViewModel movieViewModel;

    private FragmentViewpager2PersonBinding fragmentViewpager2PersonBinding;
    private ArrayList<ItemCastAndCrew> itemCastAndCrewArrayList = new ArrayList<>();
    private ArrayList<Person> personArrayList = new ArrayList<>();
    private List<Fragment> fragmentPersonArrayList = new ArrayList<>();

    private int countSuccess = 0;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void setItemCastAndCrewArrayList(ArrayList<ItemCastAndCrew> itemCastAndCrewArrayList) {
        this.itemCastAndCrewArrayList = itemCastAndCrewArrayList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentViewpager2PersonBinding = FragmentViewpager2PersonBinding.inflate(inflater, container, false);
        return fragmentViewpager2PersonBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setListPersonsFragment();
    }

    private void setListPersonsFragment() {
        Log.d(CONSTANT.TAG_VIEWPAGER2_PERSON_FRAGMENT, "setListPersonsFragment#start");
        Log.d(CONSTANT.TAG_VIEWPAGER2_PERSON_FRAGMENT, "setListPersonsFragment#itemCastAndCrewArrayList.size() = " + itemCastAndCrewArrayList.size());
        countSuccess = 0;
        for (int i = 0; i < itemCastAndCrewArrayList.size(); i++){
            int finalI = i;
            compositeDisposable.add(movieViewModel.getPersonDetailByRxJava(itemCastAndCrewArrayList.get(i).getId()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(person -> {
                        Log.d(CONSTANT.TAG_VIEWPAGER2_PERSON_FRAGMENT, "setListPersonsFragment#person.toString() = " + person.toString());
                        DetailPersonsFragment detailPersonsFragment = new DetailPersonsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt(CONSTANT.KEY_CURRENT_POSITION, finalI);
                        bundle.putParcelable(CONSTANT.KEY_SEND_PERSON, person);
                        detailPersonsFragment.setArguments(bundle);
                        fragmentPersonArrayList.add(detailPersonsFragment);
                    }, this::handleErrorGetPerson, ()->{
                        countSuccess++;
                        if (countSuccess == itemCastAndCrewArrayList.size()){
                            setUpPersonsViewPager2();
                        }
                    }));
        }
        Log.d(CONSTANT.TAG_VIEWPAGER2_PERSON_FRAGMENT, "setListPersonsFragment#end");
    }

    private void handleErrorGetPerson(Throwable error){
        Log.e(CONSTANT.TAG_VIEWPAGER2_PERSON_FRAGMENT, "handleErrorGetPerson#error.getMessage() = " + error.getMessage());
        countSuccess++;
    }

    private void setUpPersonsViewPager2() {
        if (fragmentViewpager2PersonBinding == null) return;
        fragmentViewpager2PersonBinding.progressBar.setVisibility(View.GONE);
        fragmentViewpager2PersonBinding.viewPager2Persons.setOffscreenPageLimit(fragmentPersonArrayList.size());
        FmPersonsViewPagerAdapter fmPersonsViewPagerAdapter = new FmPersonsViewPagerAdapter(requireActivity(), fragmentPersonArrayList);
        fragmentViewpager2PersonBinding.viewPager2Persons.setAdapter(fmPersonsViewPagerAdapter);
    }

    private void init() {
        if (fragmentViewpager2PersonBinding == null) return;
        Log.d(CONSTANT.TAG_VIEWPAGER2_PERSON_FRAGMENT, "setListPersonsFragment#fragmentPersonArrayList.size()="+fragmentPersonArrayList.size());
        fragmentViewpager2PersonBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentViewpager2PersonBinding.progressBar.setIndeterminate(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentViewpager2PersonBinding = null;
    }
}

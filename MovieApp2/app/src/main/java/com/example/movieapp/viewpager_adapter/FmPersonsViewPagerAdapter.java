package com.example.movieapp.viewpager_adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class FmPersonsViewPagerAdapter extends FragmentStateAdapter {

    List<Fragment> fragmentListPersons;

    public FmPersonsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragmentListPersons) {
        super(fragmentActivity);
        this.fragmentListPersons = fragmentListPersons;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentListPersons.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentListPersons.size();
    }
}

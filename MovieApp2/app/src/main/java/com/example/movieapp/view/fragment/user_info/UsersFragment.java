package com.example.movieapp.view.fragment.user_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieapp.R;
import com.example.movieapp.adapter.UserAdapter;
import com.example.movieapp.databinding.FragmentUsersBinding;
import com.example.movieapp.entity.User;
import com.example.movieapp.utils.Utils;
import com.example.movieapp.view_model.UserViewModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UsersFragment extends Fragment implements UserAdapter.UserItemClickListener
,View.OnClickListener{

    private UserViewModel userViewModel;

    FragmentUsersBinding fragmentUsersBinding;

    private UserAdapter userAdapter;

    private final ArrayList<User> userArrayList = new ArrayList<>();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentUsersBinding = FragmentUsersBinding.inflate(inflater, container, false);
        return fragmentUsersBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        initEvents();

        initData();

    }

    private void initEvents() {
        fragmentUsersBinding.btnCancel.setOnClickListener(this);
        fragmentUsersBinding.btnAddNewUser.setOnClickListener(this);
    }

    private void init() {
        userAdapter = new UserAdapter(this);
        fragmentUsersBinding.rvUserList.setAdapter(userAdapter);
        fragmentUsersBinding.rvUserList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void initData() {
        compositeDisposable.add(userViewModel.getAllUsers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                        users -> {
                            userArrayList.addAll(users);
                            userAdapter.setData(userArrayList);
                            userAdapter.notifyDataSetChanged();
                        }
                ));
    }

    @Override
    public void onItemClick(User user) {

    }

    @Override
    public void deleteUserClick(User user) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentUsersBinding = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_cancel){
            requireActivity().onBackPressed();
        }
        if (view.getId() == R.id.btn_add_new_user){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }
}

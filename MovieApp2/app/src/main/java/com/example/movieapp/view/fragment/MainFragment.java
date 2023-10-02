package com.example.movieapp.view.fragment;

import static com.example.movieapp.utils.CONSTANT.TAG_EDIT_PROFILE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.movieapp.MovieApplication;
import com.example.movieapp.R;
import com.example.movieapp.databinding.FragmentMainBinding;
import com.example.movieapp.entity.User;
import com.example.movieapp.interfa.ChangeMovieDetailTitleToolbarListener;
import com.example.movieapp.interfa.ChangeMovieListTitleToolbarListener;
import com.example.movieapp.interfa.UpdateUserInfoListener;
import com.example.movieapp.model.SharedPreferencesManager;
import com.example.movieapp.utils.CONSTANT;
import com.example.movieapp.utils.LocaleHelper;
import com.example.movieapp.utils.Utils;
import com.example.movieapp.view.fragment.fragment_movie.MovieFragment;
import com.example.movieapp.view.fragment.setting_reminder.SettingsFragment;
import com.example.movieapp.view.fragment.user_info.EditProfileFragment;
import com.example.movieapp.view.fragment.user_info.UsersFragment;
import com.example.movieapp.view_model.FavouriteViewModel;
import com.example.movieapp.view_model.MovieViewModel;
import com.example.movieapp.view_model.ReminderViewModel;
import com.example.movieapp.view_model.UserViewModel;
import com.example.movieapp.view_model.ViewModelManager;
import com.example.movieapp.viewpager_adapter.FmMoviesViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener
        , ChangeMovieDetailTitleToolbarListener, ChangeMovieListTitleToolbarListener, UpdateUserInfoListener {

    private FavouriteViewModel favouriteViewModel;
    private MovieViewModel movieViewModel;
    private ReminderViewModel reminderViewModel;
    private UserViewModel userViewModel;

    private FragmentMainBinding fragmentMainBinding;

    private final FavouriteFragment favouriteFragment = new FavouriteFragment();
    private final SettingsFragment settingsFragment = new SettingsFragment();
    private final AboutFragment aboutFragment = new AboutFragment();
    private final MovieFragment movieFragment = new MovieFragment();
    private final List<Fragment> fragmentList = new ArrayList<>();

    private AppCompatTextView btnEditProfileInfo;

    private AppCompatTextView btnSwitchProfileInfo;
    private AppCompatTextView btnShowAllRemindList;

    //Tab of tablayout
    TabLayout.Tab tab0;
    TabLayout.Tab tab1;
    TabLayout.Tab tab2;
    TabLayout.Tab tab3;

    //Item navigation view
    View headerLayout;
    AppCompatTextView tvUsernameNav;
    AppCompatTextView tvDayOfBirth;
    AppCompatTextView tvEmail;
    AppCompatTextView tvSex;
    CircleImageView circleImageViewAvatar;
    TextView tvMovieInfo1;
    TextView tvMovieInfo2;
    TextView tvMovieReminder1;
    TextView tvMovieReminder2;
    LinearLayout itemReminderDemo1;
    LinearLayout itemReminderDemo2;

    private boolean isViewGroupGenreVisible = false;

    public static boolean isLinearLayoutVerticalShowMethod = true;
    public static int currentGenreSelected = CONSTANT.MOVIE_GENRE_POPULAR;
    public static boolean isMovieFragment = true;
    public static String titleCurrentMovie = "";

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onCreate#start");
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onCreate#start#LocaleHelper.getLanguage(requireContext()) = " + LocaleHelper.getLanguage(requireContext()));
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());

        fragmentList.add(movieFragment);
        fragmentList.add(favouriteFragment);
        fragmentList.add(settingsFragment);
        fragmentList.add(aboutFragment);

        movieFragment.setChangeMovieListTitleToolbarListener(this);
        movieFragment.setChangeMovieDetailTitleToolbarListener(this);

        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onCreateView#start");
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onViewCreated#start");
        super.onViewCreated(view, savedInstanceState);

        init();

        setFragmentForTabLayoutBottom();

        initData();

        setEvents();
    }

    private void init() {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "init#start");
        fragmentMainBinding.viewPager2Movies.setOffscreenPageLimit(fragmentList.size());

        FmMoviesViewPagerAdapter fmMoviesViewPagerAdapter = new FmMoviesViewPagerAdapter(requireActivity(), fragmentList);
        fragmentMainBinding.viewPager2Movies.setAdapter(fmMoviesViewPagerAdapter);

        new TabLayoutMediator(fragmentMainBinding.tabLayoutBottom, fragmentMainBinding.viewPager2Movies
                , (tab, position) -> {
            switch (position) {
                case 0:
                case 2:
                case 3:
                    tab.setCustomView(R.layout.custom_tab_tablayout);
                    break;
                case 1:
                    tab.setCustomView(R.layout.custom_tab2_tablayout);
                    break;
            }
        }).attach();

        ((AppCompatActivity) requireActivity()).setSupportActionBar(fragmentMainBinding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(requireActivity(), fragmentMainBinding.drawerLayoutForBottom, fragmentMainBinding.toolbar,
                R.string.email_demo, R.string.copy_right_demo);
        fragmentMainBinding.drawerLayoutForBottom.addDrawerListener(toggle);
        toggle.syncState();

        setupHeaderLayoutInformation();

        //init movie list show method
        if (isLinearLayoutVerticalShowMethod)
            fragmentMainBinding.imgShowMethods.setImageResource(R.drawable.ic_outline_view_list_24);
        else
            fragmentMainBinding.imgShowMethods.setImageResource(R.drawable.ic_baseline_view_module_24);

        setViewTabLayout();

        //set current genre
        setCurrentGenreSelected();

        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "init#end");
    }

    private void initData() {
        setFavouriteNumbers();
        deleteAllOldReminder(compositeDisposable);
    }

    private void setFragmentForTabLayoutBottom() {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "setTitleToolBar#start");
        fragmentMainBinding.tabLayoutBottom.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onTabSelected#tab0");
                    fragmentMainBinding.imgSearch.setVisibility(View.GONE);
                    fragmentMainBinding.edtInputSearch.setVisibility(View.GONE);
                    View view = tab.getCustomView();
                    if (view != null) {
                        setInfoTabIsSelected(view.findViewById(R.id.img_icon_tab), view.findViewById(R.id.tv_title_tab), tab.getPosition());
                    }
                    if (isMovieFragment) {
                        fragmentMainBinding.imgShowMethods.setVisibility(View.VISIBLE);
                        fragmentMainBinding.tvTitleFragment.setVisibility(View.INVISIBLE);
                        fragmentMainBinding.tvTitleFragmentTabhome.setVisibility(View.VISIBLE);
                        fragmentMainBinding.tvTitleFragment.setText(getString(R.string.movies));
                    } else {
                        fragmentMainBinding.imgShowMethods.setVisibility(View.INVISIBLE);
                        fragmentMainBinding.tvTitleFragment.setVisibility(View.VISIBLE);
                        fragmentMainBinding.tvTitleFragmentTabhome.setVisibility(View.INVISIBLE);
                        fragmentMainBinding.tvTitleFragment.setText(titleCurrentMovie);
                    }

                    initCategory();

                } else if (tab.getPosition() == 1) {
                    Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onTabSelected#tab1");
                    fragmentMainBinding.tvTitleFragment.setText(getString(R.string.favourite));
                    fragmentMainBinding.imgShowMethods.setVisibility(View.GONE);
                    fragmentMainBinding.imgSearch.setVisibility(View.VISIBLE);
                    View view = tab.getCustomView();
                    if (view != null) {
                        setInfoTabIsSelected(view.findViewById(R.id.img_icon_tab), view.findViewById(R.id.tv_title_tab), tab.getPosition());
                    }
                } else if (tab.getPosition() == 2) {
                    Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onTabSelected#tab2");
                    fragmentMainBinding.imgShowMethods.setVisibility(View.INVISIBLE);
                    fragmentMainBinding.tvTitleFragment.setText(getString(R.string.settings));
                    fragmentMainBinding.imgSearch.setVisibility(View.GONE);
                    fragmentMainBinding.edtInputSearch.setVisibility(View.GONE);
                    fragmentMainBinding.tvTitleFragment.setVisibility(View.VISIBLE);
                    View view = tab.getCustomView();
                    if (view != null) {
                        setInfoTabIsSelected(view.findViewById(R.id.img_icon_tab), view.findViewById(R.id.tv_title_tab), tab.getPosition());
                    }
                } else if (tab.getPosition() == 3) {
                    Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onTabSelected#tab3");
                    fragmentMainBinding.imgShowMethods.setVisibility(View.INVISIBLE);
                    fragmentMainBinding.tvTitleFragment.setText(getString(R.string.about));
                    fragmentMainBinding.imgSearch.setVisibility(View.GONE);
                    fragmentMainBinding.edtInputSearch.setVisibility(View.GONE);
                    fragmentMainBinding.tvTitleFragment.setVisibility(View.VISIBLE);
                    View view = tab.getCustomView();
                    if (view != null) {
                        setInfoTabIsSelected(view.findViewById(R.id.img_icon_tab), view.findViewById(R.id.tv_title_tab), tab.getPosition());
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    View view = tab.getCustomView();
                    if (view != null) {
                        setInfoTabNormal(view.findViewById(R.id.img_icon_tab), view.findViewById(R.id.tv_title_tab), tab.getPosition());
                    }
                    fragmentMainBinding.rgGenre.setVisibility(View.GONE);
                    fragmentMainBinding.tvTitleFragmentTabhome.setVisibility(View.INVISIBLE);
                    fragmentMainBinding.tvTitleFragment.setVisibility(View.VISIBLE);
                }
                if (tab.getPosition() == 1) {
                    View view = tab.getCustomView();
                    if (view != null) {
                        setInfoTabNormal(view.findViewById(R.id.img_icon_tab), view.findViewById(R.id.tv_title_tab), tab.getPosition());
                    }
                    fragmentMainBinding.imgShowMethods.setVisibility(View.VISIBLE);
                    fragmentMainBinding.imgSearch.setVisibility(View.GONE);
                    fragmentMainBinding.imgCancelSearch.setVisibility(View.GONE);
                }
                if (tab.getPosition() == 2) {
                    View view = tab.getCustomView();
                    if (view != null) {
                        setInfoTabNormal(view.findViewById(R.id.img_icon_tab), view.findViewById(R.id.tv_title_tab), tab.getPosition());
                    }
                }
                if (tab.getPosition() == 3) {
                    View view = tab.getCustomView();
                    if (view != null) {
                        setInfoTabNormal(view.findViewById(R.id.img_icon_tab), view.findViewById(R.id.tv_title_tab), tab.getPosition());
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "setTitleToolBar#end");

    }

    private void setViewTabLayout() {
        tab0 = fragmentMainBinding.tabLayoutBottom.getTabAt(0);
        tab1 = fragmentMainBinding.tabLayoutBottom.getTabAt(1);
        tab2 = fragmentMainBinding.tabLayoutBottom.getTabAt(2);
        tab3 = fragmentMainBinding.tabLayoutBottom.getTabAt(3);
        checkTabAndSetInfoTab(tab0, 0);
        checkTabAndSetInfoTab(tab1, 1);
        checkTabAndSetInfoTab(tab2, 2);
        checkTabAndSetInfoTab(tab3, 3);

        //select tab movies
        TabLayout.Tab tab = getTabFollowPosition(0);
        if (tab != null) {
            Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "setViewTabLayout#tab != null");
            tab.select();
            View view = tab.getCustomView();
            if (view != null) {
                setInfoTabIsSelected(view.findViewById(R.id.img_icon_tab), view.findViewById(R.id.tv_title_tab), tab.getPosition());
            }
            fragmentMainBinding.tvTitleFragment.setVisibility(View.INVISIBLE);
            fragmentMainBinding.tvTitleFragmentTabhome.setVisibility(View.VISIBLE);
            fragmentMainBinding.rgGenre.setVisibility(View.GONE);
            isViewGroupGenreVisible = false;
        }
    }

    private void setFavouriteNumbers() {
        Log.d(CONSTANT.TAG_FAVOURITE_FRAGMENT, "getAllFavouriteFromDB#start");
        compositeDisposable.add(favouriteViewModel.getAllFavourite(MovieApplication.idUserCurrent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                favourites -> {
                    int numberFavourite = 0;
                    for (int i = 0; i < favourites.size(); i++){
                        if (favourites.get(i).isFavourite){
                            numberFavourite++;
                        }
                    }
                    tab1 = fragmentMainBinding.tabLayoutBottom.getTabAt(1);
                    if (tab1 != null) {
                        View view = tab1.getCustomView();
                        if (view != null) {
                            TextView tvFavouriteNumber = view.findViewById(R.id.tv_favourite_number);
                            String favouriteFilmNumber = numberFavourite + "";
                            tvFavouriteNumber.setText(favouriteFilmNumber);
                        }
                    }

                }, ViewModelManager::handleError)
        );
    }

    private void deleteAllOldReminder(CompositeDisposable compositeDisposable){
        reminderViewModel.deleteAllOldReminder(compositeDisposable, MovieApplication.idUserCurrent);
    }

    private void checkTabAndSetInfoTab(TabLayout.Tab tab, int position) {
        if (tab != null) {
            View view = tab.getCustomView();
            if (view != null) {
                setInfoTabNormal(view.findViewById(R.id.img_icon_tab), view.findViewById(R.id.tv_title_tab), position);
                TextView tvTitleTab = view.findViewById(R.id.tv_title_tab);

                if (position == 0) {
                    tvTitleTab.setText(getString(R.string.movies));
                } else if (position == 1) {
                    tvTitleTab.setText(getString(R.string.favourite));
                } else if (position == 2) {
                    tvTitleTab.setText(getString(R.string.settings));
                } else if (position == 3) {
                    tvTitleTab.setText(getString(R.string.about));
                }
            }
        }
    }

    private void setInfoTabNormal(ImageView iconImgTab, TextView tvTitleTab, int position) {
        if (position == 0) {
            iconImgTab.setImageResource(R.drawable.ic_baseline_home_24);
            tvTitleTab.setTextColor(getResources().getColor(R.color.white));
        } else if (position == 1) {
            iconImgTab.setImageResource(R.drawable.ic_baseline_favorite_24);
            tvTitleTab.setTextColor(getResources().getColor(R.color.white));
        } else if (position == 2) {
            iconImgTab.setImageResource(R.drawable.ic_baseline_settings_24);
            tvTitleTab.setTextColor(getResources().getColor(R.color.white));
        } else if (position == 3) {
            iconImgTab.setImageResource(R.drawable.ic_baseline_info_24);
            tvTitleTab.setTextColor(getResources().getColor(R.color.white));
        }
    }
    private void setupHeaderLayoutInformation() {
        headerLayout = fragmentMainBinding.navBottomTab.getHeaderView(0);
        btnEditProfileInfo = headerLayout.findViewById(R.id.btn_edit_profile);
        btnSwitchProfileInfo = headerLayout.findViewById(R.id.btn_switch_profile);
        tvUsernameNav = headerLayout.findViewById(R.id.username_nav);
        tvDayOfBirth = headerLayout.findViewById(R.id.day_of_birth);
        tvEmail = headerLayout.findViewById(R.id.email);
        tvSex = headerLayout.findViewById(R.id.sex);
        circleImageViewAvatar = headerLayout.findViewById(R.id.img_avatar_user);
        btnShowAllRemindList = headerLayout.findViewById(R.id.btn_show_all_reminders);
        tvMovieInfo1 = headerLayout.findViewById(R.id.tv_movie_info_1);
        tvMovieInfo2 = headerLayout.findViewById(R.id.tv_movie_info_2);
        tvMovieReminder1 = headerLayout.findViewById(R.id.tv_movie_reminder_1);
        tvMovieReminder2 = headerLayout.findViewById(R.id.tv_movie_reminder_2);
        itemReminderDemo1 = headerLayout.findViewById(R.id.item_reminder_demo_1);
        itemReminderDemo2 = headerLayout.findViewById(R.id.item_reminder_demo_2);

        AtomicReference<User> currentUser = new AtomicReference<>(createNewUser());

        if (MovieApplication.idUserCurrent == -1L){
            compositeDisposable.add(userViewModel.getFirstUser().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            user -> {
                                currentUser.set(user);

                                Log.d(CONSTANT.TAG_MAIN_FRAGMENT,
                                        "#setVisibleUser.idUserCurrent111 = " + currentUser.get().idUser);

                                SharedPreferencesManager.setLongValue(
                                        requireContext(),
                                        currentUser.get().idUser,
                                        SharedPreferencesManager.KEY_ID_USER_CURRENT
                                );

                                setVisibleUser(currentUser.get());
                            },
                            Throwable::printStackTrace,
                            () -> compositeDisposable.add(userViewModel.insert(currentUser.get())
                                    .subscribeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            ()->{

                                                Log.d(CONSTANT.TAG_MAIN_FRAGMENT,
                                                        "#setVisibleUser.idUserCurrent111 = " + currentUser.get().idUser);

                                                SharedPreferencesManager.setLongValue(
                                                        requireContext(),
                                                        currentUser.get().idUser,
                                                        SharedPreferencesManager.KEY_ID_USER_CURRENT
                                                );
                                                setVisibleUser(currentUser.get());
                                                Log.d(CONSTANT.TAG_USER_VIEW_MODEL, "Creating new user success!");
                                            },
                                            error -> {
                                                Log.d(CONSTANT.TAG_USER_VIEW_MODEL, "Creating new user fail!");
                                            }
                                    ))
                    )
            );
        }
        else{
            compositeDisposable.add(userViewModel.getUserById(MovieApplication.idUserCurrent).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(
                            user -> {
                                currentUser.set(user);
                                setVisibleUser(currentUser.get());
                            }
                    ));
        }

        compositeDisposable.add(reminderViewModel.get2Reminder(MovieApplication.idUserCurrent).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(reminders -> {

            if (reminders.size() >= 1) {
                itemReminderDemo1.setVisibility(View.VISIBLE);
                compositeDisposable.add(movieViewModel.getMovieDetailByRxJava(reminders.get(0).movieId)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                movie -> {
                                    movie.setReminderDateTime(reminders.get(0).reminderTime);
                                    String movieInfo = movie.getTitle() + " - " + Utils.getYear(movie.getReleaseDate()) + " - " + movie.getVoteAverage() + "/10";
                                    tvMovieInfo1.setText(movieInfo);
                                    tvMovieReminder1.setText(movie.getReminderDateTime());
                                }, ViewModelManager::handleError, ViewModelManager::handleSuccess)
                );
            }else{
                itemReminderDemo1.setVisibility(View.INVISIBLE);
                itemReminderDemo2.setVisibility(View.INVISIBLE);
            }

            if (reminders.size() >= 2) {
                itemReminderDemo2.setVisibility(View.VISIBLE);
                compositeDisposable.add(movieViewModel.getMovieDetailByRxJava(reminders.get(1).movieId)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                movie -> {
                                    movie.setReminderDateTime(reminders.get(1).reminderTime);
                                    String movieInfo = movie.getTitle() + " - " + Utils.getYear(movie.getReleaseDate()) + " - " + movie.getVoteAverage() + "/10";
                                    tvMovieInfo2.setText(movieInfo);
                                    tvMovieReminder2.setText(movie.getReminderDateTime());
                                }, ViewModelManager::handleError, ViewModelManager::handleSuccess)
                );
            }else{
                itemReminderDemo2.setVisibility(View.INVISIBLE);
            }

        }));
    }

    private void setInfoTabIsSelected(ImageView iconImgTab, TextView tvTitleTab, int position) {
        if (position == 0) {
            iconImgTab.setImageResource(R.drawable.ic_baseline_home_24_yellow);
            tvTitleTab.setTextColor(getResources().getColor(R.color.orange_FFD555));
        } else if (position == 1) {
            iconImgTab.setImageResource(R.drawable.ic_baseline_favorite_24_yellow);
            tvTitleTab.setTextColor(getResources().getColor(R.color.orange_FFD555));
        } else if (position == 2) {
            iconImgTab.setImageResource(R.drawable.ic_baseline_settings_24_yellow);
            tvTitleTab.setTextColor(getResources().getColor(R.color.orange_FFD555));
        } else if (position == 3) {
            iconImgTab.setImageResource(R.drawable.ic_baseline_info_24_yellow);
            tvTitleTab.setTextColor(getResources().getColor(R.color.orange_FFD555));
        }
    }

    private void setEvents() {
        fragmentMainBinding.imgThreeDot.setOnClickListener(this);

        //button in navigation view header
        btnEditProfileInfo.setOnClickListener(this);
        btnSwitchProfileInfo.setOnClickListener(this);

        fragmentMainBinding.imgShowMethods.setOnClickListener(this);
        btnShowAllRemindList.setOnClickListener(this);
        fragmentMainBinding.imgSearch.setOnClickListener(this);
        fragmentMainBinding.imgCancelSearch.setOnClickListener(this);
        fragmentMainBinding.tvTitleFragmentTabhome.setOnClickListener(this);
        fragmentMainBinding.rbPopularMovies.setOnClickListener(this);
        fragmentMainBinding.rbTopRatedMovies.setOnClickListener(this);
        fragmentMainBinding.rbUpcomingMovies.setOnClickListener(this);
        fragmentMainBinding.rbNowPlayingMovies.setOnClickListener(this);

        fragmentMainBinding.edtInputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                favouriteFragment.filterFilm(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_three_dot) {
            showPopupOptionMenu();
        }
        if (view.getId() == R.id.img_show_methods) {
            if (isLinearLayoutVerticalShowMethod) {
                isLinearLayoutVerticalShowMethod = false;
                fragmentMainBinding.imgShowMethods.setImageResource(R.drawable.ic_baseline_view_module_24);
            } else {
                isLinearLayoutVerticalShowMethod = true;
                fragmentMainBinding.imgShowMethods.setImageResource(R.drawable.ic_outline_view_list_24);
            }
            changeWayToShowMovieList();
        }
        if (view.getId() == R.id.btn_edit_profile) {
            openEditProfileScreen();
        }
        if (view.getId() == R.id.btn_switch_profile) {
            openSwitchProfileScreen();
        }
        if (view.getId() == R.id.btn_show_all_reminders) {
            showAllReminderList();
        }
        if (view.getId() == R.id.img_search) {
            searchListFavourite();
        }
        if (view.getId() == R.id.img_cancel_search) {
            cancelSearchListFavourite();
        }
        if (view.getId() == R.id.tv_title_fragment_tabhome) {
            showSelectMoviesGenre();
        }
        if (view.getId() == R.id.rb_popular_movies) {
            currentGenreSelected = CONSTANT.MOVIE_GENRE_POPULAR;
            setCurrentGenreSelected();
            movieFragment.setMoviesDataShowed(currentGenreSelected);
        }
        if (view.getId() == R.id.rb_top_rated_movies) {
            currentGenreSelected = CONSTANT.MOVIE_GENRE_TOP_RATED;
            setCurrentGenreSelected();
            movieFragment.setMoviesDataShowed(currentGenreSelected);
        }
        if (view.getId() == R.id.rb_upcoming_movies) {
            currentGenreSelected = CONSTANT.MOVIE_GENRE_UPCOMING;
            setCurrentGenreSelected();
            movieFragment.setMoviesDataShowed(currentGenreSelected);
        }
        if (view.getId() == R.id.rb_now_playing_movies) {
            currentGenreSelected = CONSTANT.MOVIE_GENRE_NOW_PLAYING;
            setCurrentGenreSelected();
            movieFragment.setMoviesDataShowed(currentGenreSelected);
        }
    }

    private void showPopupOptionMenu() {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "showPopupOptionMenu#start");
        PopupMenu popupMenu = new PopupMenu(requireContext(), fragmentMainBinding.imgThreeDot);
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_demo_bottom_navigation, popupMenu.getMenu());
        popupMenu.show();
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "showPopupOptionMenu#end");
    }

    private void changeWayToShowMovieList() {
        movieFragment.onClickChangeTypeDisplay();
    }

    private void openEditProfileScreen() {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "openEditProfileScreen#start");
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        if (fragmentManager.findFragmentByTag(CONSTANT.TAG_EDIT_PROFILE) == null) {
            editProfileFragment.setUpdateUserInfoListener(this);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_main, editProfileFragment, CONSTANT.TAG_EDIT_PROFILE);
            fragmentTransaction.addToBackStack(CONSTANT.BACK_TO_MAIN);
            fragmentTransaction.commit();
        }

        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "openEditProfileScreen#end");
    }

    private void openSwitchProfileScreen(){

        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "openSwitchProfileScreen#start");
        UsersFragment usersFragment = new UsersFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        if (fragmentManager.findFragmentByTag(CONSTANT.TAG_USERS_PROFILE) == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_main, usersFragment, CONSTANT.TAG_USERS_PROFILE);
            fragmentTransaction.addToBackStack(CONSTANT.BACK_TO_MAIN);
            fragmentTransaction.commit();
        }

        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "openSwitchProfileScreen#end");
    }
    private void showAllReminderList() {
        TabLayout.Tab tab = getTabFollowPosition(2);
        if (tab != null) {
            tab.select();
            fragmentMainBinding.tvTitleFragment.setText(getString(R.string.all_reminders));
        }

        settingsFragment.openReminderFragment();

        fragmentMainBinding.drawerLayoutForBottom.closeDrawer(GravityCompat.START);
    }

    private void searchListFavourite() {
        fragmentMainBinding.imgSearch.setVisibility(View.INVISIBLE);
        fragmentMainBinding.imgCancelSearch.setVisibility(View.VISIBLE);
        fragmentMainBinding.tvTitleFragment.setVisibility(View.GONE);
        fragmentMainBinding.edtInputSearch.setVisibility(View.VISIBLE);
        fragmentMainBinding.edtInputSearch.hasFocus();
    }

    private void cancelSearchListFavourite() {
        fragmentMainBinding.imgSearch.setVisibility(View.VISIBLE);
        fragmentMainBinding.imgCancelSearch.setVisibility(View.INVISIBLE);
        fragmentMainBinding.tvTitleFragment.setVisibility(View.VISIBLE);
        fragmentMainBinding.edtInputSearch.setVisibility(View.GONE);
        fragmentMainBinding.edtInputSearch.setText("");
    }

    private void showSelectMoviesGenre() {
        if (isViewGroupGenreVisible) {
            fragmentMainBinding.rgGenre.setVisibility(View.GONE);
            isViewGroupGenreVisible = false;
        } else {
            fragmentMainBinding.rgGenre.setVisibility(View.VISIBLE);
            isViewGroupGenreVisible = true;
        }
    }

    private void initCategory() {
        String currentCategory = SharedPreferencesManager.getStringValue(requireContext(), SharedPreferencesManager.KEY_MOVIE_CATEGORY, getString(R.string.popular_movies));
        if (currentCategory.equals(getString(R.string.popular_movies))){
            currentGenreSelected = CONSTANT.MOVIE_GENRE_POPULAR;
            setCurrentGenreSelected();
        }else if (currentCategory.equals(getString(R.string.top_rated_movies))){
            currentGenreSelected = CONSTANT.MOVIE_GENRE_TOP_RATED;
            setCurrentGenreSelected();
        }else if (currentCategory.equals(getString(R.string.upcoming_movies))){
            currentGenreSelected = CONSTANT.MOVIE_GENRE_UPCOMING;
            setCurrentGenreSelected();
        }else if (currentCategory.equals(getString(R.string.now_playing_movies))){
            currentGenreSelected = CONSTANT.MOVIE_GENRE_NOW_PLAYING;
            setCurrentGenreSelected();
        }else{
            currentGenreSelected = CONSTANT.MOVIE_GENRE_POPULAR;
            setCurrentGenreSelected();
        }
        movieFragment.setMoviesDataShowed(currentGenreSelected);
    }

    private void setCurrentGenreSelected() {
        if (currentGenreSelected == CONSTANT.MOVIE_GENRE_POPULAR) {
            fragmentMainBinding.tvTitleFragmentTabhome.setText(getString(R.string.popular_movies));
            fragmentMainBinding.rgGenre.check(R.id.rb_popular_movies);
        } else if (currentGenreSelected == CONSTANT.MOVIE_GENRE_TOP_RATED) {
            fragmentMainBinding.tvTitleFragmentTabhome.setText(getString(R.string.top_rated_movies));
            fragmentMainBinding.rgGenre.check(R.id.rb_top_rated_movies);
        } else if (currentGenreSelected == CONSTANT.MOVIE_GENRE_UPCOMING) {
            fragmentMainBinding.tvTitleFragmentTabhome.setText(getString(R.string.upcoming_movies));
            fragmentMainBinding.rgGenre.check(R.id.rb_upcoming_movies);
        } else if (currentGenreSelected == CONSTANT.MOVIE_GENRE_NOW_PLAYING) {
            fragmentMainBinding.tvTitleFragmentTabhome.setText(getString(R.string.now_playing_movies));
            fragmentMainBinding.rgGenre.check(R.id.rb_now_playing_movies);
        } else {
            fragmentMainBinding.tvTitleFragmentTabhome.setText(getString(R.string.popular_movies));
            fragmentMainBinding.rgGenre.check(R.id.rb_popular_movies);
        }
        fragmentMainBinding.rgGenre.setVisibility(View.GONE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_movies_nv) {
            TabLayout.Tab tab = getTabFollowPosition(0);
            if (tab != null) {
                tab.select();
            }
            return true;
        }
        if (menuItem.getItemId() == R.id.action_favourite_nv) {
            TabLayout.Tab tab = getTabFollowPosition(1);
            if (tab != null) {
                tab.select();
            }
            return true;
        }
        if (menuItem.getItemId() == R.id.action_settings_nv) {
            TabLayout.Tab tab = getTabFollowPosition(2);
            if (tab != null) {
                tab.select();
            }
            return true;
        }
        if (menuItem.getItemId() == R.id.action_about_nv) {
            TabLayout.Tab tab = getTabFollowPosition(3);
            if (tab != null) {
                tab.select();
            }
            return true;
        }
        return false;
    }

    @Nullable
    private TabLayout.Tab getTabFollowPosition(int position) {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "getTabFollowPosition#");
        return fragmentMainBinding.tabLayoutBottom.getTabAt(position);
    }

    @Override
    public void onStart() {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onStart#start");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onResume#");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onStop#");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onDestroyView#");
        super.onDestroyView();
        fragmentMainBinding = null;
    }

    @Override
    public void onDestroy() {
        Log.d(CONSTANT.TAG_MAIN_FRAGMENT, "onDestroy#");
        super.onDestroy();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    @Override
    public void updateUserInfo() {
//        setupHeaderLayoutInformation();
    }

    @Override
    public void changeMovieDetailTitleToolbar(String titleMovie) {
        fragmentMainBinding.tvTitleFragment.setVisibility(View.VISIBLE);
        fragmentMainBinding.tvTitleFragmentTabhome.setVisibility(View.INVISIBLE);
        fragmentMainBinding.tvTitleFragment.setText(titleMovie);
        if (!titleMovie.equals(getString(R.string.movies))) {
            fragmentMainBinding.imgShowMethods.setVisibility(View.INVISIBLE);
        } else {
            fragmentMainBinding.imgShowMethods.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void changeMovieListTitleToolbar(String titleFragmentMovie) {
        fragmentMainBinding.tvTitleFragment.setVisibility(View.INVISIBLE);
        fragmentMainBinding.tvTitleFragmentTabhome.setVisibility(View.VISIBLE);
        fragmentMainBinding.tvTitleFragment.setText(titleFragmentMovie);
        if (!titleFragmentMovie.equals(getString(R.string.movies))) {
            fragmentMainBinding.imgShowMethods.setVisibility(View.INVISIBLE);
        } else {
            fragmentMainBinding.imgShowMethods.setVisibility(View.VISIBLE);
        }
    }

    private User createNewUser(){
        return new User( "Name Default" , "emailDemo@gmail.com" , "1999/9/9" , CONSTANT.SEX_MALE , "" );
    }

    private void setVisibleUser(User user){
        MovieApplication.idUserCurrent = user.idUser;

        tvUsernameNav.setText(user.name);
        tvEmail.setText(user.email);
        tvDayOfBirth.setText(user.birthday);

        if (user.sex.equals(CONSTANT.SEX_MALE)) tvSex.setText(getString(R.string.sex_male));
        else tvSex.setText(getString(R.string.sex_female));

        String imgAvatar = user.avatar;

        if (!imgAvatar.equals("")){
            Log.i(CONSTANT.TAG_MAIN_FRAGMENT, "getAvatar#start");
            try {
                byte[] b = Base64.decode(imgAvatar, Base64.DEFAULT);
                Log.d(TAG_EDIT_PROFILE, "getAvatar#b.length" + b.length);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                circleImageViewAvatar.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                // java.lang.IllegalArgumentException: bad base-64
                android.util.Log.i(TAG_EDIT_PROFILE, "", e);
            }
            Log.i(CONSTANT.TAG_MAIN_FRAGMENT, "getAvatar#end");
        }
    }

}
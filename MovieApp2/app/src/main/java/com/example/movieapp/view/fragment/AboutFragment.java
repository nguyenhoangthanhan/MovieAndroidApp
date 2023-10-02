package com.example.movieapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movieapp.databinding.FragmentAboutBinding;
import com.example.movieapp.utils.Utils;

public class AboutFragment extends Fragment {

    FragmentAboutBinding fragmentAboutBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setLanguage(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAboutBinding = FragmentAboutBinding.inflate(inflater, container, false);
        return fragmentAboutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        fragmentAboutBinding.webViewAbout.setWebViewClient(new WebViewClient());
        fragmentAboutBinding.webViewAbout.loadUrl("https://www.themoviedb.org/about/our-history");
        fragmentAboutBinding.webViewAbout.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentAboutBinding = null;
    }
}

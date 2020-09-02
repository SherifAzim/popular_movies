package com.example.popularmovies.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.popularmovies.ui.details.DetailsViewModel;
import com.example.popularmovies.ui.splash.SplashViewModel;

public class SplashViewModelFactory implements ViewModelProvider.Factory {
    private Application context;

    public SplashViewModelFactory(Application context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new SplashViewModel(context);
    }
}

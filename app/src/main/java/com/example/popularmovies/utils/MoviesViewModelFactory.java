package com.example.popularmovies.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.popularmovies.ui.popularlist.PopularListViewModel;

public class MoviesViewModelFactory implements ViewModelProvider.Factory {
    private Application context;

    public MoviesViewModelFactory(Application context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new PopularListViewModel(context);
    }
}

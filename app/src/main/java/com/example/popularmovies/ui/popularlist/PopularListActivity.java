package com.example.popularmovies.ui.popularlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.popularmovies.data.models.Movie;
import com.example.popularmovies.databinding.ActivityMainBinding;
import com.example.popularmovies.ui.OnClickCallback;
import com.example.popularmovies.ui.details.DetailsActivity;
import com.example.popularmovies.utils.Constants;
import com.example.popularmovies.ui.PopularListAdapter;
import com.example.popularmovies.utils.MoviesViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class PopularListActivity extends AppCompatActivity implements OnClickCallback {

    private static final String TAG = "PopularListActivity";
    private PopularListViewModel popularListViewModel;
    private ActivityMainBinding binding;
    private RecyclerView moviesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private List<Movie> movieList;
    private int page = 1;
    private boolean isLoading = false;
    private boolean isSwipeRefresh = false;
    private PopularListAdapter popularListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        popularListViewModel = new MoviesViewModelFactory(getApplication()).create(PopularListViewModel.class);
        moviesRecyclerView = binding.popularMoviesRecycleId;
        swipeRefreshLayout = binding.popularSwipeRefreshId;
        linearLayoutManager = new LinearLayoutManager(this);
        movieList = new ArrayList<>();
        popularListAdapter = new PopularListAdapter(movieList, this, this);

        onSwipeRefreshListener();
        initMoviesList();
        onRecycleViewScrollListener();


        //first get movies form database
        popularListViewModel.getAllMoviesFromDB();
        onMoviesListObserver();
    }

    private void onMoviesListObserver() {
        popularListViewModel.moviesList.observe(this, movies -> {
            Log.d(TAG, "onchange: moviesListLiveData called");

            if (isSwipeRefresh){
                //init the recyclerView with new list
                popularListAdapter.initRecyclerViewWithData(movies);
                isLoading = false;
                isSwipeRefresh = false;

            }else{
                //Update the recyclerView the list with new page list
                popularListAdapter.updateRecyclerViewData(movies);
                isLoading = false;
            }

        });
    }


    //Handle the Pagination
    //get the current scroll position if it >= the total items then
    //increment the page number and call API
    //then in movieList observer update the UI
    private void onRecycleViewScrollListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            moviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int childCount = recyclerView.getChildCount();
                    int totalItemCount = recyclerView.getAdapter().getItemCount();
                    int lastVisibleItemPos = linearLayoutManager.findLastVisibleItemPosition();

                    int currentPos = childCount + lastVisibleItemPos;

                    if (currentPos >= totalItemCount && !isLoading) {
                        Log.d(TAG, "onScrolled: currentPos  =  " + currentPos);
                        isLoading = true;
                        //Increment the page
                        page++;
                        Log.d(TAG, "onScrolled: pages = " + page);

                        //load the next page by calling API
                        popularListViewModel.getPopularMoviesList(page);
                    }
                }
            });
        }
    }

    //handle swipe refresh action
    //call popular movies API
    //init the recyclerView with new list
    private void onSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoading = true;
                isSwipeRefresh = true;
                popularListViewModel.getPopularMoviesList(page);

                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void initMoviesList() {
        moviesRecyclerView.setAdapter(popularListAdapter);
        moviesRecyclerView.setLayoutManager(linearLayoutManager);

    }

    //On movie click
    //Goto the movieDetails activity
    //and passing the movieId
    @Override
    public void onMovieClick(Integer movieId) {
        Log.d(TAG, "onMovieClick: Called, movieId =  " + movieId);


        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constants.MOVIE_ID, movieId);
        startActivity(intent);
    }
}

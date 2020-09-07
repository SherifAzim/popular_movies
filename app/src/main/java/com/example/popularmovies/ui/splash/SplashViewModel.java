package com.example.popularmovies.ui.splash;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmovies.data.DataRepository;
import com.example.popularmovies.data.models.Movie;
import com.example.popularmovies.data.models.PopularMovies;
import com.example.popularmovies.utils.Constants;
import com.example.popularmovies.data.local.MoviesSharedPreferences;

import java.util.Date;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class SplashViewModel extends ViewModel {

    private static final String TAG = "SplashViewModel";
    private DataRepository dataRepository;
    private MoviesSharedPreferences sharedPreferences;
    public MutableLiveData<Boolean> getMoviesFromDBIsDone = new MutableLiveData<>();

    public SplashViewModel(Application context) {

        this.dataRepository = new DataRepository(context);
        sharedPreferences = new MoviesSharedPreferences(context);
        getMoviesFromDBIsDone.postValue(false);
    }

    public SplashViewModel(DataRepository dataRepository, MoviesSharedPreferences sharedPreferences) {
        this.dataRepository = dataRepository;
        this.sharedPreferences = sharedPreferences;
        //getMoviesFromDBIsDone.postValue(false);
    }

    public void getMoviesFromDB() {

        dataRepository.getAllMoviesFromDB()
                .doAfterSuccess(movies ->
                        getMoviesFromDBIsDone.postValue(true)
                )
                .subscribe(new SingleObserver<List<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe:getMoviesFromDB called ");
            }

            @Override
            public void onSuccess(List<Movie> movies) {

                if (movies.isEmpty()){
                    updateDBWithLatestData();
                }else
                Log.d(TAG, "onSuccess: getMoviesFromDB:: movie title =  " + movies.get(0).getTitle() + "size =  " + movies.size());

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public void updateDBWithLatestData() {
        //callGetPopularMoviesAPI -> calling insertMoviesIntoDB
        callGetPopularMoviesAPI();
        updateTheLastUpdateTime();
    }

    public long getLastUpdateDate(){

        return sharedPreferences.getLastUpdateDate();
    }

    public void initTheLastUpdateTime(){
        long currentTime = new Date().getTime()/1000;
        //if  getLastUpdateDate = default value
        //so that the first time
        //save the current time
        if (getLastUpdateDate() == Constants.LastUpdateTimeDefaultValue){
            sharedPreferences.saveLastUpdateTime(currentTime);
        }
    }
    public void updateTheLastUpdateTime() {

        long currentTime = new Date().getTime()/1000;
        Log.d(TAG, "updateLastUpdateFlag: currentTimeInSec = " + currentTime);
        sharedPreferences.saveLastUpdateTime(currentTime);
    }

    private void insertMoviesIntoDB(List<Movie> movies) {

        dataRepository.insertAllMoviesIntoDB(movies);
    }

    //Call popular movies API
    //Insert movies into database
    private void callGetPopularMoviesAPI() {

        dataRepository.getPopularMoviesListAPI(1).subscribe(new SingleObserver<PopularMovies>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(PopularMovies popularMovies) {

                Log.d(TAG, "onSuccess: callGetPopularMoviesAPI:: called");
                insertMoviesIntoDB(popularMovies.getMovies());
                getMoviesFromDBIsDone.postValue(true);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    //Check if last update time is less one day or it the first time run
    //if true: 1 - call movies API
    //         2-  insert movies into database
    //         3-  update the lastUpdateTime flag
    //if False: then get the movies form database
    // and goto popularMoviesActivity
    public void handleMoviesData(long timeDifference, long updatePeriod) {

        if (timeDifference > updatePeriod) {

            Log.d(TAG, "onCreate: calling updateDBWithLatestData");
            updateDBWithLatestData();
            //Update the GOTO

        } else {
            Log.d(TAG, "onCreate: calling getMoviesFromDB");
            getMoviesFromDB();

        }

    }
}

package com.example.popularmovies.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.popularmovies.R;
import com.example.popularmovies.databinding.ActivityMainBinding;
import com.example.popularmovies.databinding.ActivitySplashBinding;
import com.example.popularmovies.ui.popularlist.PopularListActivity;
import com.example.popularmovies.utils.SplashViewModelFactory;

import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private SplashViewModel splashViewModel;
    private ActivitySplashBinding splashBinding;

    private long oneDay =  (60 * 60 * 24);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(splashBinding.getRoot());


        splashViewModel = new SplashViewModelFactory(getApplication()).create(SplashViewModel.class);
        splashViewModel.initTheLastUpdateTime();


          long currentTime = new Date().getTime()/1000;
          long lastUpdateTime = splashViewModel.getLastUpdateDate();
          long timeDifference = currentTime - lastUpdateTime;

        Log.d(TAG, "onCreate: CurrentTime = " + currentTime);
        Log.d(TAG, "onCreate: LastUpdateTime = " + lastUpdateTime);
        Log.d(TAG, "onCreate: TimeDifference = " + timeDifference);

        //Check if last update time is less one day or it the first time run
        //if true: 1 - call movies API
        //         2-  insert movies into database
        //         3-  update the lastUpdateTime flag
        //if False: then get the movies form database
        // and goto popularMoviesActivity

        if (timeDifference > oneDay) {

            Log.d(TAG, "onCreate: calling updateDBWithLatestData");
            splashViewModel.updateDBWithLatestData();
            //Update the GOTO

        } else {
            Log.d(TAG, "onCreate: calling getMoviesFromDB");
            splashViewModel.getMoviesFromDB();

        }

        splashViewModel.getMoviesFromDBIsDone.observe(this, flag -> {
            Log.d(TAG, "onChanged: getMoviesFromDBIsDone called ");
            if (flag){
                Log.d(TAG, "onChanged: getMoviesFromDBIsDone Flag = True ");
                Intent intent = new Intent(SplashActivity.this, PopularListActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }


}
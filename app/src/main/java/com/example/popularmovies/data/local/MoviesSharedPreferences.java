package com.example.popularmovies.data.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.popularmovies.R;
import com.example.popularmovies.utils.Constants;

public class MoviesSharedPreferences {

    private static final String LAST_UPDATE_DATE = "lastdate";
    private Application context;
    private SharedPreferences sharedPreferences;

    public MoviesSharedPreferences(Application context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPrefrence_name), Context.MODE_PRIVATE);
    }

    public void saveLastUpdateTime(long date){

        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putLong(LAST_UPDATE_DATE,date);
        editor.apply();
    }

    public long getLastUpdateDate(){

        return sharedPreferences.getLong(LAST_UPDATE_DATE, Constants.LastUpdateTimeDefaultValue);
    }
}

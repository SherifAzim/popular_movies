package com.example.popularmovies.data.local;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.popularmovies.data.models.Genre;
import com.example.popularmovies.data.models.Movie;
import com.example.popularmovies.data.models.MovieDetails;

//movies_database
//Has three tables: movie , movie_detail and genre.

@Database(entities = {Movie.class , MovieDetails.class} , version = 3, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase instance;

    public abstract MoviesDao getMoviesDao();
    public abstract MovieDetailDao getMovieDetailDao();

    //make a MovieDatabase class as singleton class to get one instance at runtime
    public  static synchronized MoviesDatabase getInstance(Application application){

        if (instance == null){

            return Room.databaseBuilder(application,MoviesDatabase.class,"movies_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

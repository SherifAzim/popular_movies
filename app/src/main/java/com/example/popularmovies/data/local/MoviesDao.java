package com.example.popularmovies.data.local;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.popularmovies.data.models.Movie;
import java.util.List;
import io.reactivex.Single;

//Data access object class
@Dao
public abstract class MoviesDao implements BaseDao<Movie>  {


    //Select all movies in movie table
    @Query("SELECT * FROM movie")
   public abstract Single<List<Movie>> getAllMovies();

    @Query("DELETE  FROM movie")
    public abstract void deleteAllMovies();
}

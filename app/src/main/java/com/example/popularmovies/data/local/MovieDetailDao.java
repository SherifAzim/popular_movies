package com.example.popularmovies.data.local;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.popularmovies.data.models.Genre;
import com.example.popularmovies.data.models.MovieDetails;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public abstract class MovieDetailDao implements BaseDao<MovieDetails>{




    //Select all movie_details in movie_detail table
    @Query("SELECT * FROM movie_detail")
  public abstract Single<MovieDetails> getAllMovieDetails();

    //Select  movie_details in movie_detail table of movieId
    @Query("SELECT * FROM movie_detail WHERE id = :movieId")
    public abstract Single<List<MovieDetails>> getMovieDetailsOfMovieId(int movieId);

    @Query("SELECT EXISTS (SELECT * FROM movie_detail WHERE id = :movieId) ")
    public abstract Single<Boolean> isMovieDetailsExist(int movieId);
}

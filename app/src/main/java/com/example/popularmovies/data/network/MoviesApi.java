package com.example.popularmovies.data.network;

import com.example.popularmovies.data.models.Configuration;
import com.example.popularmovies.data.models.Movie;
import com.example.popularmovies.data.models.MovieDetails;
import com.example.popularmovies.data.models.PopularMovies;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    @GET("movie/popular")
    Single<PopularMovies> getPopularMovies(@Query("api_key") String apiKey,
                                           @Query("page") Integer page,
                                           @Query("language") String language,
                                           @Query("region") String region);

    @GET("configuration")
    Single<Configuration> getConfiguration(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Single<MovieDetails> getMovieDetails(@Path(value = "movie_id") Integer movieId,
                                         @Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Single<Response<MovieDetails>> getMovieDetailsResponse(@Path(value = "movie_id") Integer movieId,
                                                           @Query("api_key") String apiKey);


}

package com.example.popularmovies.data;

import android.app.Application;
import android.os.AsyncTask;

import com.example.popularmovies.data.local.MovieDetailDao;
import com.example.popularmovies.data.local.MoviesDao;
import com.example.popularmovies.data.local.MoviesDatabase;
import com.example.popularmovies.data.models.Configuration;
import com.example.popularmovies.data.models.Movie;
import com.example.popularmovies.data.models.MovieDetails;
import com.example.popularmovies.data.models.PopularMovies;
import com.example.popularmovies.data.network.ApiClient;
import com.example.popularmovies.data.network.MoviesApi;
import com.example.popularmovies.utils.Constants;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class DataRepository {

    private static final String TAG = "DataRepository";

    private MoviesApi apiClient = ApiClient.getInstance().getApiClient();
    private MoviesDatabase moviesDatabase;

    public DataRepository(Application application) {

        moviesDatabase = MoviesDatabase.getInstance(application);
    }

    public Single<List<Movie>> getAllMoviesFromDB() {

        return moviesDatabase.getMoviesDao().getAllMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());

    }

    public void insertAllMoviesIntoDB(List<Movie> movies) {

      new InsertAsyncTask(moviesDatabase.getMoviesDao(), movies, true).execute();

    }

    public Single<PopularMovies> getPopularMoviesListAPI(int page) {

        return apiClient.getPopularMovies(Constants.api_key, page, Constants.language, Constants.region)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());

    }


    public Single<MovieDetails> getMovieDetailsAPI(Integer movieId) {

        return apiClient.getMovieDetails(movieId, Constants.api_key);

    }

    public Single<List<MovieDetails>> getMovieDetailsFromDB(int movieId){
       return moviesDatabase.getMovieDetailDao()
               .getMovieDetailsOfMovieId(movieId)
               .subscribeOn(Schedulers.io())
               .observeOn(Schedulers.io());
    }

    public void insertMovieDetailsToDB(MovieDetails details) {
        new InsertAsyncTask(moviesDatabase.getMovieDetailDao(),details,false).execute();
    }

    public Single<Configuration> getConfiguration() {

        return apiClient.getConfiguration(Constants.api_key);

    }

    public Single<Boolean> isMovieDetailsExist(int movieId) {

        return moviesDatabase.getMovieDetailDao().isMovieDetailsExist(movieId)
                .subscribeOn(Schedulers.io());
    }





    private static class InsertAsyncTask extends AsyncTask<Void, Void, Boolean>{

        private MoviesDao moviesDao;
        private List<Movie> movies;
        private MovieDetailDao detailDao;
        private MovieDetails details;
        private boolean isMoviesTable = false;

        public InsertAsyncTask(MoviesDao moviesDao, List<Movie> movies, boolean isMoviesTable) {
            this.moviesDao = moviesDao;
            this.movies = movies;
            this.isMoviesTable = isMoviesTable;
        }

        public InsertAsyncTask(MovieDetailDao detailDao, MovieDetails details, boolean isMoviesTable) {
            this.detailDao = detailDao;
            this.details = details;
            this.isMoviesTable = isMoviesTable;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (isMoviesTable){
                moviesDao.insert(movies);
            }else{
               detailDao.insert(details);
            }

            return null;
        }
    }

}

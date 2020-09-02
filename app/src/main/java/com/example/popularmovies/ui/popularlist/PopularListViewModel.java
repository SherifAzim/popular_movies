package com.example.popularmovies.ui.popularlist;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmovies.data.DataRepository;
import com.example.popularmovies.data.models.Configuration;
import com.example.popularmovies.data.models.Movie;
import com.example.popularmovies.data.models.PopularMovies;
import com.example.popularmovies.utils.Constants;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PopularListViewModel extends ViewModel {

    private static final String TAG = "PopularListViewModel";
    private DataRepository dataRepository ;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MutableLiveData<List<Movie>> moviesList = new MutableLiveData<>();
    public MutableLiveData<List<Movie>> updatedMoviesAPI = new MutableLiveData<>();
    public MutableLiveData<List<Movie>> moviesListDB = new MutableLiveData<>();
    public MutableLiveData<Boolean> isMovieExist = new MutableLiveData<>();

    public PopularListViewModel(Application context) {

        dataRepository = new DataRepository(context);
    }


    public void getPopularMoviesList(int page) {
        Log.d(TAG, "getPopularMoviesList: called");

        Disposable disposable = dataRepository.getPopularMoviesListAPI(page)
                .subscribeWith(new DisposableSingleObserver<PopularMovies>() {
                    @Override
                    public void onSuccess(PopularMovies popularMovies) {

                      //  isMovieExist.postValue(!(popularMovies.getMovies().isEmpty()));
                        moviesList.postValue(popularMovies.getMovies());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: getPopularMoviesList:: " + e.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void getImageConfigurations(){

        Disposable disposable = dataRepository.getConfiguration()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<Configuration>() {
                    @Override
                    public void onSuccess(Configuration configuration) {

                        Constants.logoSizes = configuration.getImages().getLogoSizes();
                        Constants.backdropSizes = configuration.getImages().getBackdropSizes();
                       // Constants.image_url = configuration.getImages().getBaseUrl();
                        Constants.profileSizes = configuration.getImages().getProfileSizes();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: getImageConfigurations:: " + e.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void insertAllMoviesToDB(List<Movie> movies){
        Log.d(TAG, "insertAllMoviesToDB: called");

       dataRepository.insertAllMoviesIntoDB(movies);


    }

    public void getAllMoviesFromDB(){

        Log.d(TAG, "getAllMoviesFromDB: called");
        dataRepository.getAllMoviesFromDB().subscribe(new SingleObserver<List<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe:getMoviesFromDB called ");
            }

            @Override
            public void onSuccess(List<Movie> movies) {
                Log.d(TAG, "onSuccess: getAllMoviesFromDB called");
                if (movies.isEmpty()){

                    getPopularMoviesList(1);
                }else{
                    Log.d(TAG, "onSuccess: getMoviesFromDB:: movie title =  " + movies.get(0).getTitle()+ "size =  " + movies.size());
                    moviesList.postValue(movies);
                }

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

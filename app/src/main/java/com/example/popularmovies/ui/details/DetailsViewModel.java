package com.example.popularmovies.ui.details;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularmovies.data.DataRepository;
import com.example.popularmovies.data.models.MovieDetails;

import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class DetailsViewModel extends ViewModel {

    private static final String TAG = "DetailsViewModel";
    private DataRepository dataRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MutableLiveData<MovieDetails> movieDetailsLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isMovieDetailsExist = new MutableLiveData<>();


    public DetailsViewModel(Application context) {
        dataRepository = new DataRepository(context);
    }

    public DetailsViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public void isMovieDetailsExist(int movieId) {

        Disposable disposable =
                dataRepository.isMovieDetailsExist(movieId)
                        .subscribeWith(new DisposableSingleObserver<Boolean>() {
                            @Override
                            public void onSuccess(Boolean b) {

                                isMovieDetailsExist.postValue(b);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        });
        compositeDisposable.add(disposable);
    }

    public void getMovieDetailsFromDB(int movieId) {
        Log.d(TAG, "getMovieDetailsFromDB: called");
        Disposable disposable = dataRepository.getMovieDetailsFromDB(movieId)
                .subscribeWith(new DisposableSingleObserver<List<MovieDetails>>() {
                    @Override
                    public void onSuccess(List<MovieDetails> movieDetails) {

                        Log.d(TAG, "onSuccess: getMovieDetailsFromDB movieDetails.isEmpty = " + movieDetails.isEmpty());

                        if (!movieDetails.isEmpty())
                            movieDetailsLiveData.postValue(movieDetails.get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void getMovieDetailsAPI(Integer movieId) {

        Disposable disposable = dataRepository.getMovieDetailsAPI(movieId)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<MovieDetails>() {

                    @Override
                    public void onSuccess(MovieDetails movieDetails) {

                        movieDetailsLiveData.postValue(movieDetails);
                    }

                    @Override
                    public void onError(Throwable e) {
                        MovieDetails movieDetails = new MovieDetails();
                        movieDetails.setStatusMessage(e.getMessage());
                        movieDetails.setSuccess(false);

                        movieDetailsLiveData.postValue(movieDetails);
                        Log.e(TAG, "onError: getMovieDetails:: " + e.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void insertMovieDetailsToDB(MovieDetails movieDetails) {
        dataRepository.insertMovieDetailsToDB(movieDetails);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }


}

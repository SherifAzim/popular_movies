package com.example.popularmovies.ui.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import com.example.popularmovies.R;
import com.example.popularmovies.data.models.Genre;
import com.example.popularmovies.data.models.MovieDetails;
import com.example.popularmovies.databinding.ActivityDetailsBinding;
import com.example.popularmovies.utils.Constants;
import com.example.popularmovies.utils.DetailsViewModelFactory;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";
    private DetailsViewModel detailsViewModel;
    private ActivityDetailsBinding binding;
    private Integer movieId;
    private Boolean isGenreSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: called");
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        detailsViewModel = new DetailsViewModelFactory(getApplication()).create(DetailsViewModel.class);

        movieId = getIntent().getIntExtra(Constants.MOVIE_ID, 0);

        detailsViewModel.getMovieDetailsAPI(movieId);

        handleMovieDetailsData(movieId);
        movieDetailsObserver();

    }

    //check if movie_details of movieId is saved in database
    //True: retrieve it form database
    //False: call API then insert it into database
    public void handleMovieDetailsData(int movieId) {

        detailsViewModel.isMovieDetailsExist(movieId);
        detailsViewModel.isMovieDetailsExist.observe(this, isExist -> {
            Log.d(TAG, "handleMovieDetailsData: isMovieDetailsExist = " + isExist);

            //If not connected and isExist --> retrieve data and updateUI
            //If not connected and is NOT Exist --> perform onBackpressed()
            if (!isNetworkConnected()) {

                if (isExist) {

                    detailsViewModel.getMovieDetailsFromDB(movieId);

                } else {
                    onBackPressed();
                }

                //If connected and isExist --> retrieve data and updateUI
                //If connected and is NOT Exist --> call API then insert it to database
            } else {

                if (isExist) {

                    detailsViewModel.getMovieDetailsFromDB(movieId);

                } else {
                    detailsViewModel.getMovieDetailsAPI(movieId);
                }

            }

        });

    }

    private void movieDetailsObserver() {
        detailsViewModel.movieDetailsLiveData.observe(this, movieDetails -> {

            Log.d(TAG, "onChanged: movieDetailsLiveData  ::called");
            //Update UI
            updateUI(movieDetails);
            //Insert details into database
            detailsViewModel.insertMovieDetailsToDB(movieDetails);
        });
    }

    private void updateUI(MovieDetails movieDetails) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.toolbarLayout.setExpandedTitleColor(getColor(R.color.transperant));
            binding.toolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
        }
        binding.toolbarLayout.setTitle(movieDetails.getTagline());

        Picasso.get().load(Constants.image_url + Constants.BACKDROP_SIZE + movieDetails.getBackdropPath())
                .into(binding.detailsBackdropId);

        Picasso.get().load(Constants.image_url + Constants.POSTER_SIZE + movieDetails.getPosterPath())
                .into(binding.detailsPosterId);


        binding.detailsTitleId.setText(movieDetails.getTitle());
        binding.detailsRatingId.setText(String.valueOf(movieDetails.getVoteAverage()));
        binding.detailsOverviewId.setText(movieDetails.getOverview());
        binding.detailsReleaseId.setText(movieDetails.getReleaseDate());
        binding.detailsStatusId.setText(movieDetails.getStatus());

        if (!isGenreSet){

            for (Genre genre : movieDetails.getGenres()) {
                binding.detailsGenersId.append("  " + genre.getName() + " ");
            }
            isGenreSet = true;
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
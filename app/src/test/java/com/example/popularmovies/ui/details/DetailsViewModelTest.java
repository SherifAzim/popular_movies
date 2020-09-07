package com.example.popularmovies.ui.details;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.popularmovies.LiveDataTestUtil;
import com.example.popularmovies.data.local.MovieDetailDao;
import com.example.popularmovies.data.local.MoviesDao;
import com.example.popularmovies.data.local.MoviesDatabase;
import com.example.popularmovies.data.models.Movie;
import com.example.popularmovies.data.models.MovieDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.regex.Matcher;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class DetailsViewModelTest {


    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    Observer<List<Movie>> listObserver;
    private DetailsViewModel detailsViewModel;
    private MoviesDatabase database;
    private MovieDetailDao movieDetailDao;
    private MoviesDao moviesDao;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        detailsViewModel = new DetailsViewModel(ApplicationProvider.getApplicationContext());


        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        database = Room.inMemoryDatabaseBuilder(context , MoviesDatabase.class)
                .allowMainThreadQueries()
                .build();

        movieDetailDao = database.getMovieDetailDao();
        moviesDao = database.getMoviesDao();
    }

    @After
    public void tearDown() throws Exception {
        detailsViewModel = null;
        database.close();
    }

    @Test
    public void testGetMovieDetailsAPI_movieID_shouldMovieDetailID() throws InterruptedException {

        int movieID = 516486;
        detailsViewModel.getMovieDetailsAPI(movieID);

       int actual = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.movieDetailsLiveData).getId();

       assertEquals(movieID , actual);

    }

    @Test
    public void testGetMovieDetailsAPI_notFoundMovieID_getErrorMessage() throws InterruptedException {

        int movieID = 0;
        detailsViewModel.getMovieDetailsAPI(movieID);

        Boolean success = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.movieDetailsLiveData).getSuccess();
        String statusMessage = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.movieDetailsLiveData).getStatusMessage();

        assertFalse(success);
        assertFalse(statusMessage.isEmpty());
        //HTTP 404 Not Found
        //assertEquals(statusMessage, "ss");

    }

    @Test
    public void testIsMovieDetailExistDB_insert_movieID_true() throws InterruptedException {

        int movieID = 516486;
        detailsViewModel.getMovieDetailsAPI(movieID);
        MovieDetails expected = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.movieDetailsLiveData);

        //insert into database
        movieDetailDao.insert(expected);

      Boolean actual =  movieDetailDao.isMovieDetailsExist(movieID).test().values().get(0);

      assertTrue(actual);

    }
    @Test
    public void testGetMovieDetailDB_insert_movieID_shouldEqual() throws InterruptedException {

        int movieID = 516486;
        detailsViewModel.getMovieDetailsAPI(movieID);
        MovieDetails expected = LiveDataTestUtil.getOrAwaitValue(detailsViewModel.movieDetailsLiveData);

        //insert into database
        movieDetailDao.insert(expected);

        MovieDetails actual =  movieDetailDao.getAllMovieDetails().test().values().get(0);

        assertEquals(expected.getTitle(),actual.getTitle());

    }


}
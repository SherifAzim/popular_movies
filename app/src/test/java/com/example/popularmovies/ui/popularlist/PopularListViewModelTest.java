package com.example.popularmovies.ui.popularlist;

import android.app.Application;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class PopularListViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    Observer<List<Movie>> listObserver;
    private PopularListViewModel popularListViewModel;
    private MoviesDatabase database;
    private MovieDetailDao movieDetailDao;
    private MoviesDao moviesDao;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        popularListViewModel = new PopularListViewModel(ApplicationProvider.getApplicationContext());
        popularListViewModel.moviesList.observeForever(listObserver);

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        database = Room.inMemoryDatabaseBuilder(context , MoviesDatabase.class)
                .allowMainThreadQueries()
                .build();


        movieDetailDao = database.getMovieDetailDao();
        moviesDao = database.getMoviesDao();
    }

    @After
    public void tearDown() throws Exception {
        popularListViewModel = null;
        database.close();
    }

    @Test
    public void testGetPopularMoviesList_GivenPageOne_ThenMoviesListOF_20() throws InterruptedException {

        popularListViewModel.getPopularMoviesList(1);

        int expected =  LiveDataTestUtil.getOrAwaitValue(popularListViewModel.moviesList).size();

        assertEquals(expected , 20);

    }
    @Test
    public void testGetPopularMoviesFromDB_insertMovieToDB_shouldEqual() throws InterruptedException {

        popularListViewModel.getPopularMoviesList(1);
        List<Movie> expected =  LiveDataTestUtil.getOrAwaitValue(popularListViewModel.moviesList);

        moviesDao.deleteAllMovies();
        moviesDao.insert(expected);

         List<Movie> actual =  moviesDao.getAllMovies().test().values().get(0);


         assertEquals(expected.size() ,actual.size());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());



    }

    @Test
    public void testDeleteAllMoviesDB_shouldSize_zero(){
        moviesDao.deleteAllMovies();

        List<Movie> actual =  moviesDao.getAllMovies().test().values().get(0);
        assertEquals(0 ,actual.size());
    }
}
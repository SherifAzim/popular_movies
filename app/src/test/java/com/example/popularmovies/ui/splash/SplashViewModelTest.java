package com.example.popularmovies.ui.splash;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnitRunner;

import com.example.popularmovies.LiveDataTestUtil;
import com.example.popularmovies.R;
import com.example.popularmovies.data.DataRepository;
import com.example.popularmovies.data.local.MovieDetailDao;
import com.example.popularmovies.data.local.MoviesDao;
import com.example.popularmovies.data.local.MoviesDatabase;
import com.example.popularmovies.data.local.MoviesSharedPreferences;
import com.example.popularmovies.ui.details.DetailsViewModel;
import com.example.popularmovies.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class SplashViewModelTest {


    private SplashViewModel splashViewModel;
    private MoviesDatabase database;
    private MovieDetailDao movieDetailDao;
    private MoviesDao moviesDao;
    private Instrumentation instrumentation;
    private MoviesSharedPreferences sharedPreferences;
    private Context context;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        instrumentation = InstrumentationRegistry.getInstrumentation();
        context = instrumentation.getTargetContext();
        sharedPreferences = new MoviesSharedPreferences((Application) context);
        database = Room.inMemoryDatabaseBuilder(context, MoviesDatabase.class)
                .allowMainThreadQueries()
                .build();
        DataRepository dataRepository = new DataRepository(database);
        splashViewModel = new SplashViewModel(dataRepository,sharedPreferences);
        movieDetailDao = database.getMovieDetailDao();
        moviesDao = database.getMoviesDao();
    }

    @After
    public void tearDown() throws Exception {
       database.close();
        splashViewModel = null;
        sharedPreferences = null;
    }


    @Test
    public void testHandleMoviesData_timeDiffIsLess_shouldGetFormDB() throws InterruptedException {
        //Given
        long timeDiff = 10L;
        long period = 50L;
        //When
        splashViewModel.handleMoviesData(timeDiff, period);
        //Then getMoviesFromDBIsDone should return true;
        Boolean actualResult = false;
                actualResult = LiveDataTestUtil.getOrAwaitValue(splashViewModel.getMoviesFromDBIsDone);

        assertTrue(actualResult);
        //Fixed by remove getMoviesFromDBIsDone.postValue(false); from viewModel constructor.
    }


}
package com.app.movieflix.ui.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.movieflix.FakeResponseUtility
import com.app.movieflix.MainCoroutineRule
import com.app.movieflix.data.source.DataRepository
import com.app.movieflix.data.source.local.FakeLocalDataSource
import com.app.movieflix.data.source.local.ILocalDataSource
import com.app.movieflix.data.source.remote.FakeRemoteDataSource
import com.app.movieflix.data.source.remote.IRemoteDataSource
import com.app.movieflix.getOrAwaitValue
import com.app.movieflix.pojo.Movie
import com.app.movieflix.pojo.NetworkMode
import com.app.movieflix.pojo.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@ExperimentalCoroutinesApi
class MoviesViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fakeLocalDataSource: ILocalDataSource

    @Inject
    lateinit var fakeRemoteDataSource: IRemoteDataSource

    @Inject
    lateinit var dispatcher: CoroutineDispatcher

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var dataRepository: DataRepository
    private lateinit var moviesViewModel: MoviesViewModel

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        hiltRule.inject()
        dataRepository = DataRepository(fakeLocalDataSource, fakeRemoteDataSource, dispatcher)
        moviesViewModel = MoviesViewModel(dataRepository, dispatcher)
    }

    @Test
    fun getMovies_shouldReturnSuccess() = (dispatcher as TestCoroutineDispatcher).runBlockingTest {
        pauseDispatcher()
        moviesViewModel.getMovies(NetworkMode.ONLINE)

        val data1 = moviesViewModel.dataLoading.getOrAwaitValue()

        Assert.assertEquals(data1 , true)

        resumeDispatcher()

        val data2 = moviesViewModel.result.getOrAwaitValue()
        Assert.assertEquals(data2 , Result.Success(FakeResponseUtility.getResponseWithListItems().results))

        val data3 = moviesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)
    }

    @Test
    fun getMovies_shouldReturnSuccess_networkModeOffline() = (dispatcher as TestCoroutineDispatcher).runBlockingTest {
        val localList = mutableListOf(
            Movie(
                id = 436969,
                backdrop_path = "/rAgsOIhqRS6tUthmHoqnqh9PIAE.jpg",
                original_title = "The Suicide Squad",
                overview = "Supervillains Harley Quinn, Bloodsport, Peacemaker and",
                poster_path = "/kb4s0ML0iVZlG6wAKbbs9NAm6X.jpg",
                title = "The Suicide Squad",
                vote_average = 8.2f,
                release_date = "2021-07-28"

            ), Movie(
                id = 497698,
                backdrop_path = "/dq18nCTTLpy9PmtzZI6Y2yAgdw5.jpg",
                original_title = "Black Widow",
                overview = "Natasha Romanoff, also known as Black Widow",
                poster_path = "/qAZ0pzat24kLdO3o8ejmbLxyOac.jpg",
                title = "Black Widow",
                vote_average = 7.8f,
                release_date = "2021-07-07"

            ), Movie(
                id = 451048,
                backdrop_path = "/bwBmo4I3fqMsVvVtamyVJHXGnLF.jpg",
                original_title = "Jungle Cruise",
                overview = "Dr. Lily Houghton enlists the aid of wisecracking",
                poster_path = "/9dKCd55IuTT5QRs989m9Qlb7d2B.jpg",
                title = "Jungle Cruise",
                vote_average = 8f,
                release_date = "2021-07-28"

            )
        )
        (fakeLocalDataSource as FakeLocalDataSource).insertMovie(localList)
        pauseDispatcher()
        moviesViewModel.getMovies(NetworkMode.OFFLINE)

        val data1 = moviesViewModel.dataLoading.getOrAwaitValue()

        Assert.assertEquals(data1 , true)

        resumeDispatcher()

        val data2 = moviesViewModel.result.getOrAwaitValue()
        Assert.assertEquals(data2 , Result.Success(localList))

        val data3 = moviesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)
    }

    @Test
    fun getMovies_shouldReturnError() = (dispatcher as TestCoroutineDispatcher).runBlockingTest {
        (fakeRemoteDataSource as FakeRemoteDataSource).setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_ERROR)

        pauseDispatcher()
        moviesViewModel.getMovies(NetworkMode.ONLINE)

        val data1 = moviesViewModel.dataLoading.getOrAwaitValue()

        Assert.assertEquals(data1 , true)

        resumeDispatcher()

        val data2 = moviesViewModel.result.getOrAwaitValue()
        Assert.assertEquals(data2 , Result.Failure(FakeResponseUtility.getResponseWithError()))

        val data3 = moviesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)
    }

    @Test
    fun filterMovie_shouldReturnSuccess() = (dispatcher as TestCoroutineDispatcher).runBlockingTest {
        val searchQuery = "Black Widow"
        val localList = mutableListOf(
            Movie(
                id = 436969,
                backdrop_path = "/rAgsOIhqRS6tUthmHoqnqh9PIAE.jpg",
                original_title = "The Suicide Squad",
                overview = "Supervillains Harley Quinn, Bloodsport, Peacemaker and",
                poster_path = "/kb4s0ML0iVZlG6wAKbbs9NAm6X.jpg",
                title = "The Suicide Squad",
                vote_average = 8.2f,
                release_date = "2021-07-28"

            ), Movie(
                id = 497698,
                backdrop_path = "/dq18nCTTLpy9PmtzZI6Y2yAgdw5.jpg",
                original_title = "Black Widow",
                overview = "Natasha Romanoff, also known as Black Widow",
                poster_path = "/qAZ0pzat24kLdO3o8ejmbLxyOac.jpg",
                title = "Black Widow",
                vote_average = 7.8f,
                release_date = "2021-07-07"

            )
        )
        (fakeLocalDataSource as FakeLocalDataSource).insertMovie(localList)

        pauseDispatcher()
        moviesViewModel.filterMovie(searchQuery)

        val data1 = moviesViewModel.dataLoading.getOrAwaitValue()

        Assert.assertEquals(data1 , true)

        resumeDispatcher()

        val data2 = moviesViewModel.result.getOrAwaitValue()
        Assert.assertEquals(data2 , Result.Success(localList.filter { it.title.contains(searchQuery) } as MutableList<Movie>))

        val data3 = moviesViewModel.dataLoading.getOrAwaitValue()
        Assert.assertEquals(data3 , false)
    }

}
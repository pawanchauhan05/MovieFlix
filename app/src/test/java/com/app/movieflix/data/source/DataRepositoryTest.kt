package com.app.movieflix.data.source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.movieflix.FakeResponseUtility
import com.app.movieflix.MainCoroutineRule
import com.app.movieflix.data.source.local.FakeLocalDataSource
import com.app.movieflix.data.source.local.ILocalDataSource
import com.app.movieflix.data.source.remote.FakeRemoteDataSource
import com.app.movieflix.data.source.remote.IRemoteDataSource
import com.app.movieflix.pojo.Movie
import com.app.movieflix.pojo.NetworkMode
import com.app.movieflix.pojo.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
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
class DataRepositoryTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Inject
    lateinit var fakeLocalDataSource: ILocalDataSource

    @Inject
    lateinit var fakeRemoteDataSource: IRemoteDataSource

    @Inject
    lateinit var coDispatcher: CoroutineDispatcher

    private lateinit var dataRepository: DataRepository

    @Before
    fun setUp() {
        // Populate @Inject fields in test class
        hiltRule.inject()
        dataRepository = DataRepository(fakeLocalDataSource, fakeRemoteDataSource, coDispatcher)
    }

    @Test
    fun getMoviesList_shouldReturnSuccess() = mainCoroutineRule.runBlockingTest {
        val list = dataRepository.getMoviesList(mutableMapOf(), NetworkMode.ONLINE).toList()

        Assert.assertEquals(
            list, listOf(
                Result.Success(FakeResponseUtility.getResponseWithListItems().results)
            )
        )

        Assert.assertEquals(
            dataRepository.localDataSource.getMovies(),
            FakeResponseUtility.getResponseWithListItems().results
        )
    }

    @Test
    fun getMoviesList_shouldReturnError() = mainCoroutineRule.runBlockingTest {
        (fakeRemoteDataSource as FakeRemoteDataSource).setStatus(FakeRemoteDataSource.Data.SHOULD_RETURN_ERROR)

        val list = dataRepository.getMoviesList(mutableMapOf(), NetworkMode.ONLINE).toList()

        Assert.assertEquals(
            list, listOf(
                Result.Failure(FakeResponseUtility.getResponseWithError())
            )
        )
    }

    @Test
    fun getFilterMoviesList_shouldReturnSuccess() = mainCoroutineRule.runBlockingTest {
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

        val list = dataRepository.getFilterMoviesList(searchQuery).toList()

        val filterList = localList.filter { it.title.contains(searchQuery) } as MutableList

        Assert.assertEquals(
            list, listOf(
                Result.Success(filterList)
            )
        )

        Assert.assertEquals(
            localList,
            fakeLocalDataSource.getMovies()
        )
    }

    @Test
    fun deleteMovieByMovieId() = mainCoroutineRule.runBlockingTest {
        val movieId = 497698
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
        dataRepository.deleteMovieByMovieId(movieId)
        Assert.assertEquals(
            1,
            fakeLocalDataSource.getMovies().size
        )
    }
}
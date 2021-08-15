package com.app.movieflix.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.movieflix.data.source.local.dao.MovieDao
import com.app.movieflix.pojo.Movie
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var movieDao: MovieDao
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        movieDao = appDatabase.getMovieDao()
    }

    @Test
    fun insertAndReadMovies() = runBlocking {
        val movie = Movie(
            id = 436969,
            backdrop_path = "/rAgsOIhqRS6tUthmHoqnqh9PIAE.jpg",
            original_title = "The Suicide Squad",
            overview = "Supervillains Harley Quinn, Bloodsport, Peacemaker and",
            poster_path = "/kb4s0ML0iVZlG6wAKbbs9NAm6X.jpg",
            title = "The Suicide Squad",
            vote_average = 8.2f,
            release_date = "2021-07-28"

        )
        movieDao.insertMovie(movie)
        val moviesList = movieDao.getMovies()
        Assert.assertTrue(moviesList.contains(movie))
    }

    @Test
    fun insertAndReadMovieList() = runBlocking {
        val movie1 = Movie(
            id = 436969,
            backdrop_path = "/rAgsOIhqRS6tUthmHoqnqh9PIAE.jpg",
            original_title = "The Suicide Squad",
            overview = "Supervillains Harley Quinn, Bloodsport, Peacemaker and",
            poster_path = "/kb4s0ML0iVZlG6wAKbbs9NAm6X.jpg",
            title = "The Suicide Squad",
            vote_average = 8.2f,
            release_date = "2021-07-28"

        )

        val movie2 = Movie(
            id = 497698,
            backdrop_path = "/dq18nCTTLpy9PmtzZI6Y2yAgdw5.jpg",
            original_title = "Black Widow",
            overview = "Natasha Romanoff, also known as Black Widow",
            poster_path = "/qAZ0pzat24kLdO3o8ejmbLxyOac.jpg",
            title = "Black Widow",
            vote_average = 7.8f,
            release_date = "2021-07-07"

        )

        val listToInsert = mutableListOf<Movie>(movie1, movie2)
        movieDao.insertMovie(listToInsert)
        val movieList = movieDao.getMovies()
        Assert.assertEquals(movieList, listToInsert)
    }

    @Test
    fun insertAndReadFilterMovieList() = runBlocking {
        val movie1 = Movie(
            id = 436969,
            backdrop_path = "/rAgsOIhqRS6tUthmHoqnqh9PIAE.jpg",
            original_title = "The Suicide Squad",
            overview = "Supervillains Harley Quinn, Bloodsport, Peacemaker and",
            poster_path = "/kb4s0ML0iVZlG6wAKbbs9NAm6X.jpg",
            title = "The Suicide Squad",
            vote_average = 8.2f,
            release_date = "2021-07-28"

        )
        val movie2 = Movie(
            id = 497698,
            backdrop_path = "/dq18nCTTLpy9PmtzZI6Y2yAgdw5.jpg",
            original_title = "Black Widow",
            overview = "Natasha Romanoff, also known as Black Widow",
            poster_path = "/qAZ0pzat24kLdO3o8ejmbLxyOac.jpg",
            title = "Black Widow",
            vote_average = 7.8f,
            release_date = "2021-07-07"

        )
        val query = "Black Widow"
        val listToInsert = mutableListOf<Movie>(movie1, movie2)
        movieDao.insertMovie(listToInsert)
        val movieList = movieDao.getFilterMovies(query)
        Assert.assertEquals(movieList, mutableListOf(movie2))
    }

    @Test
    fun insertAndDeleteMovieById() = runBlocking {
        val movie1 = Movie(
            id = 436969,
            backdrop_path = "/rAgsOIhqRS6tUthmHoqnqh9PIAE.jpg",
            original_title = "The Suicide Squad",
            overview = "Supervillains Harley Quinn, Bloodsport, Peacemaker and",
            poster_path = "/kb4s0ML0iVZlG6wAKbbs9NAm6X.jpg",
            title = "The Suicide Squad",
            vote_average = 8.2f,
            release_date = "2021-07-28"

        )
        val movie2 = Movie(
            id = 497698,
            backdrop_path = "/dq18nCTTLpy9PmtzZI6Y2yAgdw5.jpg",
            original_title = "Black Widow",
            overview = "Natasha Romanoff, also known as Black Widow",
            poster_path = "/qAZ0pzat24kLdO3o8ejmbLxyOac.jpg",
            title = "Black Widow",
            vote_average = 7.8f,
            release_date = "2021-07-07"

        )

        val movieId = 497698
        val listToInsert = mutableListOf(movie1, movie2)
        movieDao.insertMovie(listToInsert)
        movieDao.deleteMovieByMovieId(movieId)
        val movieList = movieDao.getMovies()
        Assert.assertEquals(movieList.size, 1)
    }


    @Test
    fun insertAndDeleteMovie() = runBlocking {
        val movie = Movie(
            id = 436969,
            backdrop_path = "/rAgsOIhqRS6tUthmHoqnqh9PIAE.jpg",
            original_title = "The Suicide Squad",
            overview = "Supervillains Harley Quinn, Bloodsport, Peacemaker and",
            poster_path = "/kb4s0ML0iVZlG6wAKbbs9NAm6X.jpg",
            title = "The Suicide Squad",
            vote_average = 8.2f,
            release_date = "2021-07-28"

        )
        movieDao.insertMovie(movie)
        movieDao.clearAllMovie()
        val movieList = movieDao.getMovies()

        Assert.assertTrue(movieList.isEmpty())
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

}
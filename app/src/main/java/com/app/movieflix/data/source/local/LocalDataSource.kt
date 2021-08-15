package com.app.movieflix.data.source.local

import com.app.movieflix.data.source.local.dao.MovieDao
import com.app.movieflix.pojo.Movie
import kotlinx.coroutines.CoroutineDispatcher

class LocalDataSource internal constructor(private val movieDao: MovieDao, dispatcher: CoroutineDispatcher) :
    ILocalDataSource {
    override fun insertMovie(movieList: MutableList<Movie>) {
        movieDao.insertMovie(movieList)
    }

    override fun getMovies(): MutableList<Movie> {
        return movieDao.getMovies()
    }

    override fun getFilterMovies(searchQuery: String): MutableList<Movie> {
        return movieDao.getFilterMovies(searchQuery)
    }

    override fun deleteMovieByMovieId(movieId: Int) {
        movieDao.deleteMovieByMovieId(movieId)
    }

    override fun clearAllMovie() {
        movieDao.clearAllMovie()
    }
}
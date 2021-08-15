package com.app.movieflix.data.source.local

import com.app.movieflix.pojo.Movie


interface ILocalDataSource {

    fun insertMovie(movieList: MutableList<Movie>)

    fun getMovies(): MutableList<Movie>

    fun getFilterMovies(searchQuery : String): MutableList<Movie>

    fun deleteMovieByMovieId(movieId: Int)

    fun clearAllMovie()
}
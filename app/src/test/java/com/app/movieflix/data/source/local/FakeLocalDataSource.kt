package com.app.movieflix.data.source.local

import com.app.movieflix.pojo.Movie

class FakeLocalDataSource(private var movieList: MutableList<Movie> = mutableListOf()) : ILocalDataSource {

    override fun insertMovie(movieList: MutableList<Movie>) {
        this.movieList.addAll(movieList)
    }

    override fun getMovies(): MutableList<Movie> {
        return movieList
    }

    override fun getFilterMovies(searchQuery: String): MutableList<Movie> {
        return movieList.filter { it.title.contains(searchQuery) } as MutableList<Movie>
    }

    override fun deleteMovieByMovieId(movieId: Int) {
        movieList.removeIf {
            it.id == movieId
        }
    }

    override fun clearAllMovie() {
        movieList.clear()
    }


}
package com.app.movieflix.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.movieflix.pojo.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movieList: MutableList<Movie>)

    @Query("SELECT * FROM Movie")
    fun getMovies(): MutableList<Movie>

    @Query("SELECT * FROM Movie WHERE title LIKE :searchQuery")
    fun getFilterMovies(searchQuery : String): MutableList<Movie>

    @Query("DELETE FROM Movie WHERE movie_id = :movieId")
    fun deleteMovieByMovieId(movieId: Int)

    @Query("DELETE FROM Movie")
    fun clearAllMovie()
}
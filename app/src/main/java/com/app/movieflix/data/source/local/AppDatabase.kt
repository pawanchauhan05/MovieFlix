package com.app.movieflix.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.movieflix.data.source.local.dao.MovieDao
import com.app.movieflix.pojo.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}
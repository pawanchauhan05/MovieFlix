package com.app.movieflix.di

import android.content.Context
import androidx.room.Room
import com.app.movieflix.data.source.local.AppDatabase
import com.app.movieflix.data.source.local.ILocalDataSource
import com.app.movieflix.data.source.local.LocalDataSource
import com.app.movieflix.utilities.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDataSourceModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            Config.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(appDatabase: AppDatabase, dispatcher: CoroutineDispatcher) : ILocalDataSource {
        return LocalDataSource(appDatabase.getMovieDao(), dispatcher)
    }

}
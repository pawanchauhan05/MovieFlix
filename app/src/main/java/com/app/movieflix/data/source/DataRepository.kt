package com.app.movieflix.data.source

import com.app.movieflix.data.source.local.ILocalDataSource
import com.app.movieflix.data.source.remote.IRemoteDataSource
import com.app.movieflix.pojo.NetworkMode
import com.app.movieflix.pojo.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataRepository(
    public val localDataSource: ILocalDataSource,
    public val remoteDataSource: IRemoteDataSource,
    public val dispatcher: CoroutineDispatcher
) : IDataRepository {
    override suspend fun getMoviesList(
        queryParam: MutableMap<String, String>,
        networkMode: NetworkMode
    ): Flow<Result> =
        flow {
            try {
                val localSourceList = localDataSource.getMovies()
                if (localSourceList.isNotEmpty()) {
                    emit(Result.Success(localSourceList))
                }
                if(networkMode == NetworkMode.ONLINE) {
                    val response = remoteDataSource.getMovies(queryParam)
                    response?.results?.let {
                        localDataSource.clearAllMovie()
                        localDataSource.insertMovie(it)
                        emit(Result.Success(localDataSource.getMovies()))
                    }
                }
            } catch (ex: Exception) {
                emit(Result.Failure(ex))
            }
        }

    override suspend fun getFilterMoviesList(searchQuery: String): Flow<Result> = flow {
        try {
            val filterList = localDataSource.getFilterMovies(searchQuery)
            emit(Result.Success(filterList))
        } catch (ex: Exception) {
            emit(Result.Failure(ex))
        }
    }

    override suspend fun deleteMovieByMovieId(movieId: Int) {
        localDataSource.deleteMovieByMovieId(movieId)
    }
}
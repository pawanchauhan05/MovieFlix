package com.app.movieflix.data.source

import com.app.movieflix.pojo.NetworkMode
import com.app.movieflix.pojo.Result
import kotlinx.coroutines.flow.Flow

interface IDataRepository {
    suspend fun getMoviesList(queryParam : MutableMap<String, String>, networkMode: NetworkMode)  : Flow<Result>

    suspend fun getFilterMoviesList(searchQuery : String)  : Flow<Result>

    suspend fun deleteMovieByMovieId(movieId : Int)
}
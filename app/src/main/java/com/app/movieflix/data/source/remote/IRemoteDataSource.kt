package com.app.movieflix.data.source.remote

import com.app.movieflix.pojo.ServerResponse

interface IRemoteDataSource {

    suspend fun getMovies(queryParam : MutableMap<String, String>) : ServerResponse

}
package com.app.movieflix.data.source.remote

import com.app.movieflix.pojo.ServerResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RemoteApi {

    @GET("3/movie/now_playing")
    suspend fun getMovies(@QueryMap queryParams : Map<String, String>) : ServerResponse
}
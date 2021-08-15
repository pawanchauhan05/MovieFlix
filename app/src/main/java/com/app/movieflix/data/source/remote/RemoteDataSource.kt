package com.app.movieflix.data.source.remote

import com.app.movieflix.pojo.ServerResponse

class RemoteDataSource(private val remoteApi: RemoteApi) : IRemoteDataSource {
    override suspend fun getMovies(queryParam: MutableMap<String, String>): ServerResponse {
        return remoteApi.getMovies(queryParam)
    }
}
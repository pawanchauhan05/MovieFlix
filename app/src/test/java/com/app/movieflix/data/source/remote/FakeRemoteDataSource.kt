package com.app.movieflix.data.source.remote

import com.app.movieflix.FakeResponseUtility
import com.app.movieflix.pojo.ServerResponse

class FakeRemoteDataSource : IRemoteDataSource {

    enum class Data {
        SHOULD_RETURN_ERROR,
        SHOULD_RETURN_LIST_WITH_ITEM
    }

    private var status = Data.SHOULD_RETURN_LIST_WITH_ITEM

    fun setStatus(value: Data) {
        status = value
    }

    override suspend fun getMovies(queryParam: MutableMap<String, String>): ServerResponse {
        return when(status) {
            Data.SHOULD_RETURN_ERROR -> throw FakeResponseUtility.getResponseWithError()
            Data.SHOULD_RETURN_LIST_WITH_ITEM -> FakeResponseUtility.getResponseWithListItems()
        }
    }
}
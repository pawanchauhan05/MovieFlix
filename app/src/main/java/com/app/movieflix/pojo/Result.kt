package com.app.movieflix.pojo

sealed class Result {
    data class Success(val mutableList: MutableList<Movie>) : Result()
    data class Failure(val ex: Exception) : Result()
    data class Progress(val isLoading: Boolean) : Result()
}

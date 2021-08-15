package com.app.movieflix

import com.app.movieflix.pojo.Movie
import com.app.movieflix.pojo.ServerResponse
import java.net.SocketTimeoutException

object FakeResponseUtility {
    private val ex: Exception = SocketTimeoutException("TIMEOUT ERROR!")
    private val emptyList = emptyList<Movie>()

    fun getResponseWithListItems(): ServerResponse {
        val movie1 = Movie(
            id = 436969,
            backdrop_path = "/rAgsOIhqRS6tUthmHoqnqh9PIAE.jpg",
            original_title = "The Suicide Squad",
            overview = "Supervillains Harley Quinn, Bloodsport, Peacemaker and",
            poster_path = "/kb4s0ML0iVZlG6wAKbbs9NAm6X.jpg",
            title = "The Suicide Squad",
            vote_average = 8.2f,
            release_date = "2021-07-28"

        )

        val movie2 = Movie(
            id = 497698,
            backdrop_path = "/dq18nCTTLpy9PmtzZI6Y2yAgdw5.jpg",
            original_title = "Black Widow",
            overview = "Natasha Romanoff, also known as Black Widow",
            poster_path = "/qAZ0pzat24kLdO3o8ejmbLxyOac.jpg",
            title = "Black Widow",
            vote_average = 7.8f,
            release_date = "2021-07-07"

        )

        return ServerResponse(mutableListOf<Movie>(movie1, movie2))


    }

    fun getResponseWithError(): Exception {
        return ex
    }
}
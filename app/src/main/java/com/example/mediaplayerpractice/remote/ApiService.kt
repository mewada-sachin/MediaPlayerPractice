package com.example.mediaplayerpractice.remote

import com.example.mediaplayerpractice.data.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/")
    fun getPopularMovies(
        @Query("apiKey") apiKey: String,
        @Query("i") id: String
    ): Call<Movie>
}
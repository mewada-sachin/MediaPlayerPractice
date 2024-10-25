package com.example.mediaplayerpractice.repository

import com.example.mediaplayerpractice.data.Movie
import com.example.mediaplayerpractice.remote.ApiService
import retrofit2.Call

class NetworkRepositoryImpl(private val apiService: ApiService) : NetworkRepository {
    override suspend fun getMoviesList(): Call<Movie> {
        return apiService.getPopularMovies("5dcd6c3b", "tt3896198")
    }
}
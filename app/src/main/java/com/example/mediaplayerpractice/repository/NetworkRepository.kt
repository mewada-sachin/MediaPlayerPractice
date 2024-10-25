package com.example.mediaplayerpractice.repository

import com.example.mediaplayerpractice.data.Movie
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

interface NetworkRepository {
    suspend fun getMoviesList(): Call<Movie>
}
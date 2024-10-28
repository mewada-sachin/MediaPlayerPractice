package com.example.mediaplayerpractice.recyclerview

import com.example.mediaplayerpractice.data.model.MovieDetails

interface MovieClickListener {
    fun onMoviePosterClick(movieDetails: MovieDetails)
}
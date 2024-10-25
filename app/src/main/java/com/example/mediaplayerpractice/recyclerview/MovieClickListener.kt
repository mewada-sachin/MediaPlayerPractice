package com.example.mediaplayerpractice.recyclerview

import com.example.mediaplayerpractice.data.MovieDetails

interface MovieClickListener {
    fun onMoviePosterClick(movieDetails: MovieDetails)
}
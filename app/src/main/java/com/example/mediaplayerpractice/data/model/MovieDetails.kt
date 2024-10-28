package com.example.mediaplayerpractice.data.model

data class MovieDetails(
    val id: Int,
    val cid: Int,
    val title: String,
    val year: String,
    val description: String,
    val genre: String,
    val director: String,
    val cast: String,
    val duration: String,
    val ratingName: String,
    val poster_url: String,
    val posterFilename: String,
    var isSelected: Boolean = false,
    var url: String? = null
)

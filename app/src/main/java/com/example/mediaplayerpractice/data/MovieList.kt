package com.example.mediaplayerpractice.data

data class CategoryList(val category: List<MoviesCategory>)
data class MoviesCategory(val title: String, val movies_list: List<MovieDetails>)

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

package com.example.mediaplayerpractice.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Poster")
    val poster: String
)
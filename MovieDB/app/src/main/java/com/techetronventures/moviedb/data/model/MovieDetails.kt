package com.techetronventures.moviedb.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    @SerializedName("id")
    val id: Int,

    @SerializedName("results")
    val results: List<MovieResult>
)
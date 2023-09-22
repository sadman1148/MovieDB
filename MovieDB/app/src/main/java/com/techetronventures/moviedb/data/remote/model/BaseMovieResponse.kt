package com.techetronventures.moviedb.data.remote.model

import com.google.gson.annotations.SerializedName
import com.techetronventures.moviedb.data.model.Movie

data class BaseMovieResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val results: List<Movie>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int
)
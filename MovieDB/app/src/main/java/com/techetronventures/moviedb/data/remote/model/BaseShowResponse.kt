package com.techetronventures.moviedb.data.remote.model

import com.google.gson.annotations.SerializedName

data class BaseShowResponse(
    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val results: List<Show>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int
)

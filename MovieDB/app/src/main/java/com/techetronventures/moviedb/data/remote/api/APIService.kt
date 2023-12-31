package com.techetronventures.moviedb.data.remote.api

import com.techetronventures.moviedb.data.model.MovieDetails
import com.techetronventures.moviedb.data.remote.model.BaseMovieResponse
import com.techetronventures.moviedb.data.remote.model.BaseShowResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET(APIUrl.TRENDING_MOVIE)
    suspend fun getMovieList(@Query("page") page:Int) : BaseMovieResponse

    @GET(APIUrl.TRENDING_SHOW)
    suspend fun getShowList(@Query("page") page:Int) : BaseShowResponse

    @GET("movie/{movie_id}/videos?api_key=${APIUrl.API_KEY}")
    suspend fun getMovieTrailerYoutubeId(@Path(value = "movie_id") movieId: String): MovieDetails

    @GET(APIUrl.SEARCH_URL)
    suspend fun getSearchResults(@Query("query") keywords: String): BaseMovieResponse
}
package com.techetronventures.moviedb.data.remote.api

import com.techetronventures.moviedb.data.remote.model.BaseMovieResponse
import com.techetronventures.moviedb.data.remote.model.BaseTvResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET(APIUrl.TRENDING_MOVIE)
    suspend fun getMovieList(@Query("page") page:Int) : BaseMovieResponse

    @GET(APIUrl.TRENDING_SHOW)
    suspend fun getShowList(@Path("page") page:Int) : BaseTvResponse
}
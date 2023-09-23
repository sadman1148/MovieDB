package com.techetronventures.moviedb.data.remote.api

object APIUrl {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "c33832f707ec95387239c7014b8fb76b"
    const val TRENDING_MOVIE = "trending/movie/day?api_key=${API_KEY}&language=en-US"
    const val TRENDING_SHOW = "trending/tv/day?api_key=${API_KEY}&language=en-US"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original/"
    const val SEARCH_URL = "search/movie?api_key=${API_KEY}"
}
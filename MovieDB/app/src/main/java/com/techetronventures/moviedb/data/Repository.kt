package com.techetronventures.moviedb.data

import com.techetronventures.moviedb.data.remote.api.APIService
import com.techetronventures.moviedb.utils.State
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: APIService) {

    fun getMediaList(pageNumber: Int, isMovie: Boolean) = flow {
        emit(State.Loading)
        try {
            val result = if (isMovie) apiService.getMovieList(pageNumber) else apiService.getShowList(pageNumber)
            emit(State.Success(result))
        } catch (exception: Exception) {
            emit(State.Error(exception))
        }
    }
}
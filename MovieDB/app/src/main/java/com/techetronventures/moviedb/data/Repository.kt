package com.techetronventures.moviedb.data

import com.techetronventures.moviedb.data.remote.api.APIService
import com.techetronventures.moviedb.data.remote.model.BaseMovieResponse
import com.techetronventures.moviedb.data.remote.model.BaseTvResponse
import com.techetronventures.moviedb.utils.State
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: APIService) {

    fun getMovieList(pageNumber: Int) = flow<State<BaseMovieResponse>> {
        emit(State.Loading)
        try {
            val result = apiService.getMovieList(pageNumber)
            emit(State.Success(result))
        } catch (exception: Exception) {
            emit(State.Error(exception))
        }
    }

    fun getShowList(pageNumber: Int) = flow<State<BaseTvResponse>> {
        emit(State.Loading)
        try {
            val result = apiService.getShowList(pageNumber)
            emit(State.Success(result))
        } catch (exception: Exception) {
            emit(State.Error(exception))
        }
    }
}
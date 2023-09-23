package com.techetronventures.moviedb.data

import com.techetronventures.moviedb.data.local.MovieDao
import com.techetronventures.moviedb.data.model.Movie
import com.techetronventures.moviedb.data.remote.api.APIService
import com.techetronventures.moviedb.data.remote.model.BaseMovieResponse
import com.techetronventures.moviedb.data.remote.model.BaseShowResponse
import com.techetronventures.moviedb.utils.State
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: APIService, private val movieDao: MovieDao) {

    fun getMovieList(pageNumber: Int) = flow<State<BaseMovieResponse>> {
        emit(State.Loading)
        try {
            val result = apiService.getMovieList(pageNumber)
            emit(State.Success(result))
        } catch (exception: Exception) {
            emit(State.Error(exception))
        }
    }

    fun getShowList(pageNumber: Int) = flow<State<BaseShowResponse>> {
        emit(State.Loading)
        try {
            val result = apiService.getShowList(pageNumber)
            emit(State.Success(result))
        } catch (exception: Exception) {
            emit(State.Error(exception))
        }
    }

    fun getMovieTrailerYoutubeId(movieId: String) = flow {
        emit(State.Loading)
        try {
            val result = apiService.getMovieTrailerYoutubeId(movieId)
            emit(State.Success(result))
        } catch (exception: Exception) {
            emit(State.Error(exception))
        }
    }

    suspend fun addToFavorites(movie: Movie) {
        movieDao.insert(movie)
    }

    suspend fun isMovieInDatabase(movieId: Int): Boolean {
        return movieDao.isMovieInDatabase(movieId)
    }

    suspend fun deleteById(movieId: Int) {
        movieDao.deleteById(movieId)
    }

    suspend fun getFavoriteMovies(): List<Movie> {
        return movieDao.getAllMovies()
    }
}
package com.techetronventures.moviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techetronventures.moviedb.data.Repository
import com.techetronventures.moviedb.data.model.Movie
import com.techetronventures.moviedb.data.remote.model.BaseMovieResponse
import com.techetronventures.moviedb.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val movieListLiveData: LiveData<State<BaseMovieResponse>> get() = _movieListMutableLiveData
    private val _movieListMutableLiveData = MutableLiveData<State<BaseMovieResponse>>()


    suspend fun addToFavorites(movie: Movie) {
        repository.addToFavorites(movie)
    }

    suspend fun isMovieInDatabase(movieId: Int): Boolean {
        return repository.isMovieInDatabase(movieId)
    }

    suspend fun deleteById(movieId: Int) {
        repository.deleteById(movieId)
    }

}
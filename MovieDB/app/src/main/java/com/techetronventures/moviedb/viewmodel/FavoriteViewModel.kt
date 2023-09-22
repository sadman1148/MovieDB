package com.techetronventures.moviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techetronventures.moviedb.data.Repository
import com.techetronventures.moviedb.data.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var _favoriteMovieListMutableLiveData =  MutableLiveData<List<Movie>>()
    val favoriteMovieListLiveData: LiveData<List<Movie>> = _favoriteMovieListMutableLiveData

    suspend fun getFavoriteMovies() {
        _favoriteMovieListMutableLiveData.value = repository.getFavoriteMovies()
    }

    fun deleteById(movieId: Int) {
        viewModelScope.launch {
            repository.deleteById(movieId)
        }.invokeOnCompletion {
            viewModelScope.launch {
                getFavoriteMovies()
            }
        }
    }
}
package com.techetronventures.moviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techetronventures.moviedb.data.Repository
import com.techetronventures.moviedb.data.model.MovieDetails
import com.techetronventures.moviedb.data.remote.model.BaseMovieResponse
import com.techetronventures.moviedb.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _searchedMovieListMutableLiveData = MutableLiveData<State<BaseMovieResponse>>()
    val searchedMovieListLiveData: LiveData<State<BaseMovieResponse>> get() = _searchedMovieListMutableLiveData

    var totalPages = 0

    fun getSearchResults(keywords: String) {
        viewModelScope.launch {
            repository.getSearchResults(keywords).onEach {
                _searchedMovieListMutableLiveData.value = it
            }.launchIn(viewModelScope)
        }
    }
}
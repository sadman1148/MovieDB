package com.techetronventures.moviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techetronventures.moviedb.data.Repository
import com.techetronventures.moviedb.data.remote.model.BaseMovieResponse
import com.techetronventures.moviedb.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val movieListLiveData: LiveData<State<BaseMovieResponse>> get() = _movieListMutableLiveData
    private val _movieListMutableLiveData = MutableLiveData<State<BaseMovieResponse>>()

    fun getMovieList(pageNumber: Int) {
        viewModelScope.launch {
            repository.getMovieList(pageNumber).onEach {
                _movieListMutableLiveData.value = it
            }.launchIn(viewModelScope)
        }
    }

}
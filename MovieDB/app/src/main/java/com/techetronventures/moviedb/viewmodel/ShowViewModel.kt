package com.techetronventures.moviedb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techetronventures.moviedb.data.Repository
import com.techetronventures.moviedb.data.remote.model.BaseShowResponse
import com.techetronventures.moviedb.utils.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val showListLiveData: LiveData<State<BaseShowResponse>> get() = _showListMutableLiveData
    private val _showListMutableLiveData = MutableLiveData<State<BaseShowResponse>>()

    var totalPages = 0

    fun getShowList(pageNumber: Int) {
        repository.getShowList(pageNumber).onEach {
            _showListMutableLiveData.value = it
        }.launchIn(viewModelScope)
    }
}
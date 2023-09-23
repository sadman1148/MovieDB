package com.techetronventures.moviedb.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.databinding.FragmentSearchBinding
import com.techetronventures.moviedb.ui.adapter.SearchAdapter
import com.techetronventures.moviedb.utils.State
import com.techetronventures.moviedb.utils.Utility
import com.techetronventures.moviedb.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private var isLoading = false
    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        binding.searchRecycler.layoutManager = StaggeredGridLayoutManager(2, 1)
        binding.searchRecycler.adapter = searchAdapter
        binding.searchfield.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                performOnClick()
                true
            } else {
                false
            }
        })
        binding.searchButton.setOnClickListener {
            performOnClick()
        }
        return binding.root
    }

    private fun performOnClick() {
        if (binding.searchfield.text.isBlank()) {
            Toast.makeText(requireContext(), getString(R.string.input_field_is_empty), Toast.LENGTH_SHORT).show()
            binding.searchfield.post { binding.searchfield.requestFocus() }
        } else {
            try {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
            } catch (e: Exception) {
                Log.d("SearchFragment", "Keyboard close exception: ${e.message}")
            }
            searchViewModel.getSearchResults(binding.searchfield.text.toString().replace(" ","+"))
            binding.searchfield.clearFocus()
            searchViewModel.searchedMovieListLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is State.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                    }

                    is State.Success -> {
                        isLoading = false
                        binding.loader.visibility = View.GONE
                        searchViewModel.totalPages = it.data.totalPages
                        searchAdapter.addItems(it.data.results)
                        if (searchAdapter.itemCount == 0) {
                            binding.noItemLl.visibility = View.VISIBLE
                        } else {
                            binding.noItemLl.visibility = View.GONE
                        }
                    }

                    is State.Error -> {
                        binding.loader.visibility = View.GONE
                        if (!Utility.hasInternet(requireContext())) {
                            Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        binding.searchfield.text.clear()
    }
}
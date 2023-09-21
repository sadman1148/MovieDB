package com.techetronventures.moviedb.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.techetronventures.moviedb.databinding.FragmentMovieBinding
import com.techetronventures.moviedb.ui.adapter.MovieAdapter
import com.techetronventures.moviedb.utils.State
import com.techetronventures.moviedb.utils.Utility
import com.techetronventures.moviedb.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private val movieViewModel : MovieViewModel by viewModels()
    private var pageNumber = 1
    private var isLoading = false
    private var totalPages = 0
    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter()
    }
    private lateinit var binding : FragmentMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel.getMovieList(pageNumber++)
        movieViewModel.movieListLiveData.observe(this) {
            when (it) {
                is State.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }

                is State.Success -> {
                    isLoading = false
                    binding.loader.visibility = View.GONE
                    totalPages = it.data.totalPages
                    movieAdapter.addItems(it.data.results)
                }

                is State.Error -> {
                    if (!Utility.hasInternet(requireContext())) {
                        Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMovieBinding.inflate(layoutInflater)
        binding.movieRecycler.layoutManager = StaggeredGridLayoutManager(2, 1)
        binding.movieRecycler.adapter = movieAdapter
        binding.movieRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && !isLoading && pageNumber <= totalPages) {
                    isLoading = true
                    movieViewModel.getMovieList(pageNumber++)
                }
            }
        })
        return binding.root
    }
}
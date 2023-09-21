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
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.databinding.FragmentShowBinding
import com.techetronventures.moviedb.ui.adapter.ShowAdapter
import com.techetronventures.moviedb.utils.State
import com.techetronventures.moviedb.utils.Utility
import com.techetronventures.moviedb.viewmodel.ShowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowFragment : Fragment() {

    private val showViewModel : ShowViewModel by viewModels()
    private var pageNumber = 1
    private var isLoading = false
    private val showAdapter: ShowAdapter by lazy {
        ShowAdapter(requireContext())
    }
    private lateinit var binding : FragmentShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showViewModel.getShowList(pageNumber++)
        showViewModel.showListLiveData.observe(this) {
            when (it) {
                is State.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }

                is State.Success -> {
                    isLoading = false
                    binding.loader.visibility = View.GONE
                    showViewModel.totalPages = it.data.totalPages
                    showAdapter.addItems(it.data.results)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShowBinding.inflate(layoutInflater)
        binding.showRecycler.layoutManager = StaggeredGridLayoutManager(2, 1)
        binding.showRecycler.adapter = showAdapter
        binding.showRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && !isLoading && pageNumber <= showViewModel.totalPages) {
                    isLoading = true
                    showViewModel.getShowList(pageNumber++)
                }
            }
        })
        return binding.root
    }
}
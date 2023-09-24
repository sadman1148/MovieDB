package com.techetronventures.moviedb.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.techetronventures.moviedb.databinding.FragmentFavoriteBinding
import com.techetronventures.moviedb.ui.adapter.FavoriteAdapter
import com.techetronventures.moviedb.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter(requireContext(), favoriteViewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        binding.favoriteRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteRecycler.adapter = favoriteAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.getFavoriteMovies()
        }.invokeOnCompletion {
            favoriteViewModel.favoriteMovieListLiveData.observe(viewLifecycleOwner) {
                favoriteAdapter.addItems(it)
                if (favoriteAdapter.itemCount == 0) {
                    binding.noFavoriteLl.visibility = View.VISIBLE
                } else {
                    binding.noFavoriteLl.visibility = View.GONE
                }
            }
        }
        return binding.root
    }
}
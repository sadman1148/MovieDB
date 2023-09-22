package com.techetronventures.moviedb.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.techetronventures.moviedb.databinding.FragmentFavoriteBinding
import com.techetronventures.moviedb.ui.adapter.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        binding.favoriteRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteRecycler.adapter = favoriteAdapter
        return binding.root
    }
}
package com.techetronventures.moviedb.ui.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.data.remote.api.APIUrl
import com.techetronventures.moviedb.data.remote.model.Movie
import com.techetronventures.moviedb.databinding.RecyclerItemBinding

class MovieAdapter() : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private val movies = mutableListOf<Movie>()

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.name.text = movie.title
            Glide.with(this.itemView)
                .load(APIUrl.IMAGE_BASE_URL + movie.posterPath)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("movie_data", Gson().toJson(movies[position]))
            it.findNavController().navigate(R.id.action_movieFragment_to_detailFragment, bundle)
        }
    }

    override fun getItemCount() = movies.size
}
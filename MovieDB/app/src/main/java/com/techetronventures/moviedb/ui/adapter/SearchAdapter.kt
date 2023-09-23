package com.techetronventures.moviedb.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.gson.Gson
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.data.model.Movie
import com.techetronventures.moviedb.data.remote.api.APIUrl
import com.techetronventures.moviedb.databinding.RecyclerItemBinding
import com.techetronventures.moviedb.utils.Constants

class SearchAdapter(private val context: Context) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val movies = mutableListOf<Movie>()

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(movies: List<Movie>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.name.text = movie.title
            binding.loader.visibility = View.VISIBLE
            Glide.with(this.itemView)
                .load(APIUrl.IMAGE_BASE_URL + movie.posterPath)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.loader.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.loader.visibility = View.GONE
                        binding.deadlink.visibility = View.VISIBLE
                        return false
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.KEY_MOVIE_DATA, Gson().toJson(movies[position]))
            it.findNavController().navigate(R.id.action_searchFragment_to_movieDetailFragment, bundle)
        }
    }

    override fun getItemCount() = movies.size
}
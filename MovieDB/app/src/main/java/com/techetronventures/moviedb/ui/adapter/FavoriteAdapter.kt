package com.techetronventures.moviedb.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.data.remote.api.APIUrl
import com.techetronventures.moviedb.data.model.Movie
import com.techetronventures.moviedb.databinding.FavoriteRecyclerBinding
import com.techetronventures.moviedb.viewmodel.FavoriteViewModel

class FavoriteAdapter(private val context: Context, private val favoriteViewModel: FavoriteViewModel) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val movies = mutableListOf<Movie>()

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(movies: List<Movie>) {
        this.movies.clear()
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: FavoriteRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.favoriteDetails.text = context.resources.getString(R.string.favorite_details,movie.title,movie.releaseDate)
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
                        Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                        return false
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.favoriteImage)
            binding.cross.setOnClickListener {
                favoriteViewModel.deleteById(movie.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoriteRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size
}
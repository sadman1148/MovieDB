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
import com.techetronventures.moviedb.data.remote.api.APIUrl
import com.techetronventures.moviedb.data.model.Show
import com.techetronventures.moviedb.databinding.RecyclerItemBinding

class ShowAdapter(private val context: Context) : RecyclerView.Adapter<ShowAdapter.ViewHolder>() {

    private val shows = mutableListOf<Show>()

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(movies: List<Show>) {
        this.shows.addAll(movies)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(show: Show) {
            binding.name.text = show.name
            binding.loader.visibility = View.VISIBLE
            Glide.with(this.itemView)
                .load(APIUrl.IMAGE_BASE_URL + show.posterPath)
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
                .into(binding.poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowAdapter.ViewHolder, position: Int) {
        holder.bind(shows[position])
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("show_data", Gson().toJson(shows[position]))
            it.findNavController().navigate(R.id.action_showFragment_to_showDetailFragment, bundle)
        }
    }

    override fun getItemCount() = shows.size
}
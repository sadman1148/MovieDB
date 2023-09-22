package com.techetronventures.moviedb.ui.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.data.model.Movie
import com.techetronventures.moviedb.data.remote.api.APIUrl
import com.techetronventures.moviedb.databinding.FragmentMovieDetailBinding
import com.techetronventures.moviedb.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.Type

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var movie: Movie
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMovieDetailBinding.inflate(layoutInflater)
        movie = getMovie()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.apply {
            binding.loader.visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(APIUrl.IMAGE_BASE_URL + movie.backdropPath)
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
                        Toast.makeText(requireContext(), getString(R.string.failed_to_banner_image), Toast.LENGTH_SHORT).show()
                        return false
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(backdrop)
            title.text = movie.title
            overview.text = getString(R.string.overview, movie.overview)
            releaseDate.text = getString(R.string.released_on, movie.releaseDate)
            popularity.text = getString(R.string.popularity_score, movie.popularity.toString())
            votes.text = getString(R.string.votes, movie.voteCount.toString())
            watchTrailerCard.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEARCH)
                intent.setPackage("com.google.android.youtube")
                intent.putExtra("query", "${movie.title} trailer")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleFavorites()
    }

    private fun getMovie(): Movie {
        val movie: Type = object : TypeToken<Movie>() {}.type
        return Gson().fromJson(arguments?.getString("movie_data"), movie)
    }

    private fun handleFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (movieViewModel.isMovieInDatabase(movie.id)) {
                setUiForSelectedFavorite()
            } else {
                binding.addToFavoriteCard.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        movieViewModel.addToFavorites(movie)
                        setUiForSelectedFavorite()
                        binding.addToFavoriteCard.isClickable = false
                    }
                }
            }
        }
    }

    private fun setUiForSelectedFavorite() {
        binding.addToFavoriteText.text = getString(R.string.its_a_favorite)
        binding.favoriteImage.setImageResource(R.drawable.favorite)
    }
}
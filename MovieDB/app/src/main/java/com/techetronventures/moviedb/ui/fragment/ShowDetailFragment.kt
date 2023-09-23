package com.techetronventures.moviedb.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.data.remote.api.APIUrl
import com.techetronventures.moviedb.data.model.Show
import com.techetronventures.moviedb.databinding.FragmentShowDetailBinding
import com.techetronventures.moviedb.utils.Constants
import java.lang.reflect.Type

class ShowDetailFragment : Fragment() {

    private lateinit var binding: FragmentShowDetailBinding
    private lateinit var show: Show

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentShowDetailBinding.inflate(layoutInflater)
        show = getShow()
        findNavController().graph.label = show.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.apply {
            binding.loader.visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(APIUrl.IMAGE_BASE_URL + show.backdropPath)
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
            name.text = show.name
            overview.text = getString(R.string.overview, show.overview)
            firstAirDate.text = getString(R.string.first_aired_on, show.firstAirDate, show.originCountry)
            popularity.text = getString(R.string.popularity_score, show.popularity.toString())
            votes.text = getString(R.string.votes, show.voteCount.toString())
            return root
        }
    }

    private fun getShow(): Show {
        val show: Type = object : TypeToken<Show>() {}.type
        return Gson().fromJson(arguments?.getString(Constants.KEY_SHOW_DATA), show)
    }
}
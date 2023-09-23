package com.techetronventures.moviedb.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.databinding.FragmentTrailerBinding
import com.techetronventures.moviedb.utils.Constants
import com.techetronventures.moviedb.utils.State
import com.techetronventures.moviedb.utils.Utility
import com.techetronventures.moviedb.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrailerFragment : Fragment() {

    private var isLoading = false
    private lateinit var binding: FragmentTrailerBinding
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTrailerBinding.inflate(layoutInflater)
        binding.loader.visibility = View.VISIBLE
        movieViewModel.getMovieTrailerYoutubeId((requireArguments().getInt(Constants.KEY_MEDIA_ID)).toString())
        movieViewModel.movieDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }

                is State.Success -> {
                    isLoading = false
                    binding.loader.visibility = View.GONE
                    if (it.data.results.isNotEmpty()) {
                        val trailerId = it.data.results[0].key
                        Log.d("TrailerFragment", "Media ID: ${it.data.id}")
                        Log.d("TrailerFragment", "Trailer ID: $trailerId")
                        val mediaPlayerHtml = "\n" +
                                "<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<body>\n" +
                                "<div id=\"player\"></div>\n" +
                                "\n" +
                                "<script>\n" +
                                "\n" +
                                "var tag = document.createElement('script');\n" +
                                "\n" +
                                "tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                                "var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                                "firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                                "\n" +
                                "var player;\n" +
                                "function onYouTubeIframeAPIReady() {\n" +
                                "    player = new YT.Player('player', {\n" +
                                "        height: '225',\n" +
                                "        width: '400',\n" +
                                "        videoId: '${trailerId}',\n" +
                                "        playerVars: {\n" +
                                "        'playsinline': 1\n" +
                                "    },\n" +
                                "        events: {\n" +
                                "        'onReady': onPlayerReady,\n" +
                                "        'onStateChange': onPlayerStateChange\n" +
                                "    }\n" +
                                "    });\n" +
                                "}\n" +
                                "\n" +
                                "function onPlayerReady(event) {\n" +
                                "    event.target.playVideo();\n" +
                                "}\n" +
                                "\n" +
                                "var done = false;\n" +
                                "function onPlayerStateChange(event) {\n" +
                                "    if (event.data == YT.PlayerState.PLAYING && !done) {\n" +
                                "        setTimeout(stopVideo, 100000);\n" +
                                "        done = true;\n" +
                                "    }\n" +
                                "}\n" +
                                "function stopVideo() {\n" +
                                "    player.stopVideo();\n" +
                                "}\n" +
                                "</script>\n" +
                                "</body>\n" +
                                "</html>"
                        binding.mediaPlayerWebview.settings.javaScriptEnabled = true
                        binding.mediaPlayerWebview.loadData(mediaPlayerHtml, "text/html", "utf-8")
                    } else {
                        binding.mediaPlayerWebview.visibility = View.GONE
                        binding.noTrailerTv.visibility = View.VISIBLE
                        binding.sadEmoji.visibility = View.VISIBLE
                    }
                }

                is State.Error -> {
                    binding.loader.visibility = View.GONE
                    if (!Utility.hasInternet(requireContext())) {
                        Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return binding.root
    }

}
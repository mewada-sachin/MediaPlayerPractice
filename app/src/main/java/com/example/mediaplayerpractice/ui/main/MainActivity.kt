package com.example.mediaplayerpractice.ui.main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mediaplayerpractice.R
import com.example.mediaplayerpractice.data.model.MovieDetails
import com.example.mediaplayerpractice.databinding.ActivityMainBinding
import com.example.mediaplayerpractice.recyclerview.MovieClickListener
import com.example.mediaplayerpractice.recyclerview.MoviesCategoryAdapter
import com.example.mediaplayerpractice.ui.mediaplayer.MediaPlayerActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MovieClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObserver()
    }

    private fun initObserver() {
        viewModel.liveData.observe(this) {
            binding.moviesCategory.layoutManager = LinearLayoutManager(this)
            binding.moviesCategory.adapter = MoviesCategoryAdapter(it, this)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getMoviesList("bollywood_movies")
    }

    override fun onMoviePosterClick(movieDetails: MovieDetails) {
        binding.apply {
            movieName.text = movieDetails.title
            description.text = movieDetails.description
            val layoutParams = posterImageView.layoutParams
            posterImageView.layoutParams = layoutParams
            director.text = Html.fromHtml(getString(R.string.director, movieDetails.director))
            duration.text = "Duration: ${movieDetails.duration}"
            stars.text = "Stars: ${movieDetails.cast}"
            Glide.with(this@MainActivity)
                .load(movieDetails.poster_url)
                .into(posterImageView)

            Glide.with(this@MainActivity)
                .load(movieDetails.poster_url)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        resource.alpha = 30
                        binding.root.background = resource
                        binding.root.foreground = ColorDrawable(Color.parseColor("#00000000"))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        binding.root.background = placeholder
                    }
                })

            movieName.visibility = View.VISIBLE
            description.visibility = View.VISIBLE
            posterImageView.visibility = View.VISIBLE
        }

        if (MediaPlayerActivity.isActivityRunning) {
            val intent = Intent("com.example.mediaplayer.ACTION_STOP_PIP")
            intent.putExtra("url", movieDetails.url)
            sendBroadcast(intent)
        }
        val intent = Intent(this@MainActivity, MediaPlayerActivity::class.java)
        intent.putExtra("url", movieDetails.url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}